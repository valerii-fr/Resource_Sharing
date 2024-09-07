package dev.nordix.server_provider.data

import android.util.Log
import dev.nordix.core.Constants.ROOT_SERVICE_PORT
import dev.nordix.core.Constants.ROOT_WS_PATH
import dev.nordix.server_provider.domain.WssServerProvider
import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.ActionSerializer.serviceInteractionJson
import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.actions.ServiceActionResult
import dev.nordix.services.domain.model.actions.ServiceInteraction
import dev.nordix.services.domain.model.actions.ServicesPresentationAction.ServerPresentation
import dev.nordix.services.utils.act
import dev.nordix.settings.TerminalRepository
import io.ktor.server.application.install
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.routing.routing
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import kotlin.apply
import kotlin.collections.forEach
import kotlin.collections.mapNotNull
import kotlin.let
import kotlin.reflect.full.superclasses

class WssServerProviderImpl @javax.inject.Inject constructor(
    private val services: Set<@JvmSuppressWildcards NordixTcpService<*, *>>,
    private val terminalRepository: TerminalRepository,
) : WssServerProvider {

    override fun getServer(): NettyApplicationEngine {
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE);
        val environment = applicationEngineEnvironment {
            connector {
                port = ROOT_SERVICE_PORT
                host = "0.0.0.0"
            }

            module {
                install(WebSockets.Plugin)
                routing {
                    webSocket(ROOT_WS_PATH) {
                        selfPresent()
                        observeMessages()
                    }
                }
            }
        }

        return embeddedServer(Netty, environment).apply {
            this.environment.connectors.forEach { connector ->
                Log.i(TAG, "Server started at ${connector.host}:${connector.port}")
            }
        }
    }

    private suspend fun DefaultWebSocketServerSession.selfPresent() {
        val servicesPresentation = ServerPresentation(
            terminalId = terminalRepository.terminal.id.value,
            timestamp = Instant.now(),
            serviceAliases = services.mapNotNull { it::class.superclasses.first().qualifiedName }
        )
        send(Frame.Text(
            serviceInteractionJson
                .encodeToString<ServiceInteraction>(servicesPresentation)
        ))
    }

    private suspend fun DefaultWebSocketServerSession.observeMessages() {
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val receivedText = frame.readText()
                Log.i(TAG, "Received text message: $receivedText")
                try {
                    val action = Json.decodeFromString<ServiceInteraction>(receivedText)
                    when(action) {
                        is ServiceAction<*> -> {
                            services.act(action)?.let { result ->
                                try {
                                    send(
                                        Frame.Text(
                                            serviceInteractionJson.encodeToString<ServiceActionResult>(
                                                result
                                            )
                                        )
                                    )
                                } catch (e: Throwable) {
                                    Log.e(TAG, "error on parsing action result", e)
                                }
                            }
                        }
                        is ServiceActionResult -> {
                            Log.d(TAG, "Received services action result: $action")
                        }
                    }
                } catch (e: Throwable) {
                    Log.e(TAG, "error on parsing action", e)
                }
            }
        }
        Log.e(TAG, "ws session closed")
    }

    companion object {
        private const val TAG = "WssServerProviderImpl"
    }
}
