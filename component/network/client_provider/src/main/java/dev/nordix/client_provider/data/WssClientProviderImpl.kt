package dev.nordix.client_provider.data

import android.util.Log
import dev.nordix.client_provider.domain.WssClientProvider
import dev.nordix.client_provider.domain.model.ClientTarget
import dev.nordix.services.ServiceRepository
import dev.nordix.services.domain.ActionSerializer.serviceInteractionJson
import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.actions.ServiceActionResult
import dev.nordix.services.domain.model.actions.ServiceInteraction
import dev.nordix.services.domain.model.actions.ServicesPresentationAction.ClientPresentation
import dev.nordix.services.domain.model.actions.ServicesPresentationAction.ServerPresentation
import dev.nordix.settings.TerminalRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readBytes
import io.ktor.websocket.readReason
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class WssClientProviderImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val terminalRepository: TerminalRepository,
    private val serviceRepository: ServiceRepository,
) : WssClientProvider {

    private val services = serviceRepository.observeServices()

    override val activeClients: StateFlow<List<ClientTarget>>
        field = MutableStateFlow(emptyList())

    private val activeSessions: MutableMap<ClientTarget, DefaultClientWebSocketSession> = mutableMapOf()


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
            keys.firstOrNull { it.host == host }?.let(::remove)
        }
    }

    private suspend fun DefaultClientWebSocketSession.selfPresent() {
        val clientPresentation = ClientPresentation(
            terminalId = terminalRepository.terminal.id.value,
            name = terminalRepository.terminal.name,
            serviceAliases = services.first().mapNotNull { it::class.qualifiedName }
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
                        val receivedAction = serviceInteractionJson.decodeFromString<ServiceInteraction>(text)
                        Log.i(TAG, "Received services presentation: $receivedAction")
                        when (receivedAction) {
                            is ServiceAction<*> -> {
                                Log.i(TAG, "Received services action: $receivedAction")
                                when (receivedAction) {
                                    is ServerPresentation -> {
                                        Log.i(TAG, "Received services presentation: $receivedAction")
                                    }
                                    else -> {
                                        Log.i(TAG, "Received unsupported client action: $receivedAction")
                                    }
                                }
                            }
                            is ServiceActionResult -> {
                                Log.i(TAG, "Received services action result: $receivedAction")
                            }
                            else -> {Log.i(TAG, "Received unknown action: $receivedAction") }
                        }
                    } catch (e: Throwable) {
                        Log.e(TAG, "error on parsing action", e)
                    }
                }
                is Frame.Binary -> Log.i(TAG, "Received binary message: ${message.readBytes().joinToString(", ")}")
                is Frame.Ping -> Log.i(TAG, "Received ping")
                is Frame.Pong -> Log.i(TAG, "Received pong")
                is Frame.Close -> {
                    Log.i(TAG, "Connection closed: ${message.readReason()}")
                    close()
                }
            }
        }
        Log.d(TAG, "Connection closed")
    }

    companion object {
        private const val TAG = "WssClientProviderImpl"
    }

}
