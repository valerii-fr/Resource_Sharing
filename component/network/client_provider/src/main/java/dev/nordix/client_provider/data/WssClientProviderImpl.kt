package dev.nordix.client_provider.data

import android.util.Log
import dev.nordix.client_provider.domain.WssClientProvider
import dev.nordix.client_provider.domain.model.ClientTarget
import dev.nordix.service_manager.domain.model.ServiceState.ServiceStatus
import dev.nordix.service_manager.holder.NsdServicesStateProvider
import dev.nordix.services.ServiceRepository
import dev.nordix.services.domain.ActionSerializer.serviceInteractionJson
import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.actions.ServiceActionResult
import dev.nordix.services.domain.model.actions.ServiceInteraction
import dev.nordix.services.domain.model.actions.ServicesPresentationAction
import dev.nordix.services.domain.model.actions.ServicesPresentationAction.ClientPresentation
import dev.nordix.services.domain.model.actions.ServicesPresentationAction.ServerPresentation
import dev.nordix.settings.TerminalRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readReason
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.reflect.full.superclasses

class WssClientProviderImpl @Inject constructor(
    scope: CoroutineScope,
    private val httpClient: HttpClient,
    private val terminalRepository: TerminalRepository,
    private val serviceRepository: ServiceRepository,
    private val serviceStateProvider: NsdServicesStateProvider,
) : WssClientProvider {

    private val services = serviceRepository.observeServices()
    private val activeSessions: MutableMap<ClientTarget, DefaultClientWebSocketSession> = mutableMapOf()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val resolvedServices = serviceStateProvider.debounce(1000L).mapLatest {
        it.resolvedServiceStates
    }

    override val activeClients: StateFlow<List<ClientTarget>>
        field = MutableStateFlow(emptyList())

    init {
        resolvedServices.onEach { resolvedServices ->
            broadcastInteraction(ClientPresentation(
                terminalId = terminalRepository.terminal.id.value,
                name = terminalRepository.terminal.name,
                knownDevices = resolvedServices.map { it.serviceInfo.deviceId },
                serviceAliases = services.first().mapNotNull { it::class.superclasses.first().qualifiedName },
                presentationType = ServicesPresentationAction.PresentationType.PRESENTATION_UPDATE
            ))
        }
            .flowOn(Dispatchers.IO)
            .launchIn(scope)

        flow {
            while (true) {
                emit(Unit)
                delay(5000L)
            }
        }
            .onEach {
                broadcastInteraction(ClientPresentation(
                    terminalId = terminalRepository.terminal.id.value,
                    name = terminalRepository.terminal.name,
                    knownDevices = serviceStateProvider.value.resolvedServiceStates.map { it.serviceInfo.deviceId },
                    serviceAliases = services.first().mapNotNull { it::class.superclasses.first().qualifiedName },
                    presentationType = ServicesPresentationAction.PresentationType.PRESENTATION_UPDATE
                ))
            }
            .flowOn(Dispatchers.IO)
            .launchIn(scope)
    }

    override suspend fun launchClient(
        target: ClientTarget
    ) {
        try {
            httpClient.webSocket(
                method = HttpMethod.Get,
                host = target.host,
                port = target.port,
                path = target.path
            ) {
                Log.d(this::class.simpleName, "Connection established")
                activeClients.update { clients ->
                    clients.toMutableList().apply {
                        add(target)
                        activeSessions.put(target, this@webSocket)
                    }
                }
                selfPresent()
                observeMessages()
            }
        } catch (e: Throwable) {
            Log.e(TAG, "error on launching client", e)
        }
    }

    override suspend fun terminateClient(host: String) {
        activeClients.update { clients ->
            clients.toMutableList().apply {
                removeAll { it.host == host }
            }
        }
        activeSessions.apply {
            keys.firstOrNull { it.host == host }?.let(::remove)?.close()
        }
    }

    override suspend fun postInteraction(
        target: ClientTarget,
        action: ServiceInteraction
    ) {
        Log.i(TAG, "postInteraction: $action to $target")
        activeSessions[target]?.send(Frame.Text(
            serviceInteractionJson.encodeToString<ServiceInteraction>(
                ServiceInteraction.serializer(),
                action
            )
        ))
    }

    override suspend fun broadcastInteraction(action: ServiceInteraction) {
        activeSessions.forEach { session ->
            session.value.send(Frame.Text(
                serviceInteractionJson.encodeToString<ServiceInteraction>(
                    ServiceInteraction.serializer(),
                    action
                )
            ))
            Log.d(TAG, "broadcastInteraction: $action to ${session.key}")
        }
    }

    private suspend fun DefaultClientWebSocketSession.selfPresent() {
        val clientPresentation = ClientPresentation(
            terminalId = terminalRepository.terminal.id.value,
            name = terminalRepository.terminal.name,
            knownDevices = serviceStateProvider.value.resolvedServiceStates.map { it.serviceInfo.deviceId },
            serviceAliases = services.first().mapNotNull { it::class.superclasses.first().qualifiedName }
        )
        send(Frame.Text(
            serviceInteractionJson.encodeToString<ServiceInteraction>(
                ServiceInteraction.serializer(),
                clientPresentation
            )
        ))
    }

    private suspend fun DefaultClientWebSocketSession.observeMessages() {
        for (message in incoming) {
            when (message) {
                is Frame.Text -> {
                    val text = message.readText()
                    Log.i(TAG, "Received text message: $text")
                    try {
                        serviceInteractionJson
                            .decodeFromString<ServiceInteraction>(text)
                            .process()
                    } catch (e: Throwable) {
                        Log.e(TAG, "error on parsing action", e)
                    }
                }
                is Frame.Close -> {
                    Log.i(TAG, "Connection closed: ${message.readReason()}")
                    close()
                }
                else -> { }
            }
        }
        Log.d(TAG, "Connection closed")
        this.call.request.url.host
        activeSessions.filter { it.value.hashCode() == this@observeMessages.hashCode() }.keys.onEach { target ->
            serviceStateProvider.update { servicesState ->
                servicesState.copy(
                    resolvedServiceStates = servicesState.resolvedServiceStates.toMutableList().apply {
                        val tI = indexOfFirst { service ->
                            Log.w(TAG, "checking host $service.serviceInfo.address?.hostAddress")
                            service.serviceInfo.address?.hostAddress == target.host
                        }
                        if (tI > -1) {
                            val targetValue = get(tI)
                            this[tI] = targetValue.copy(
                                status = ServiceStatus.Disconnected,
                            )
                        } else {
                            Log.w(TAG, "Closed unassigned session: ${target.host}")
                        }
                    }
                )
            }
        }
    }

    private fun ServiceInteraction.process() {
        Log.i(TAG, "Received interaction: $this")
        when (this) {
            is ServiceAction<*> -> {
                when (this) {
                    is ServerPresentation -> process()
                    else -> Log.i(TAG, "Received client action: $this")
                }
            }

            is ServiceActionResult -> Log.i(TAG, "Received services action result: $this")

            else -> Log.i(TAG, "Received unknown action: $this")
        }

    }

    private fun ServerPresentation.process() {
        Log.i(TAG, "Received services presentation: $this")
        serviceStateProvider.update { servicesState ->
            servicesState.copy(
                resolvedServiceStates = servicesState.resolvedServiceStates.toMutableList().apply {
                    val tI = indexOfFirst { service ->
                        service.terminalId == terminalId
                    }
                    if (tI > -1) {
                        val targetValue = get(tI)
                        this[tI] = targetValue.copy(
                            status = ServiceStatus.Connected,
                            serviceInfo = targetValue.serviceInfo.copy(
                                serviceAliases = serviceAliases,
                                knownDevices = knownDevices
                            )
                        )
                    } else {
                        Log.w(TAG, "Received presentation from unknown terminal: $terminalId")
                    }
                }
            )
        }
    }

    companion object {
        private const val TAG = "WssClientProviderImpl"
    }

}
