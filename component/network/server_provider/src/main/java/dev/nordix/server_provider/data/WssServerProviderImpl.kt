package dev.nordix.server_provider.data

import android.util.Log
import dev.nordix.core.Constants.ROOT_SERVICE_PORT
import dev.nordix.core.Constants.ROOT_WS_PATH
import dev.nordix.server_provider.domain.WssServerProvider
import dev.nordix.services.domain.ActionSerializer.actionResultJson
import dev.nordix.services.domain.model.ServicesPresentation
import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.results.ServiceActionResult
import dev.nordix.services.utils.act
import io.ktor.server.application.install
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import kotlin.apply
import kotlin.collections.forEach
import kotlin.collections.mapNotNull
import kotlin.let

class WssServerProviderImpl @javax.inject.Inject constructor(
    private val services: Set<@JvmSuppressWildcards dev.nordix.services.NordixTcpService<*, *>>
) : WssServerProvider {

    override fun getServer(): io.ktor.server.netty.NettyApplicationEngine {

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
                Log.i(this::class.simpleName, "Server started at ${connector.host}:${connector.port}")
            }
        }
    }

    private suspend fun DefaultWebSocketServerSession.selfPresent() {
        val servicesPresentation = ServicesPresentation(
            timestamp = Instant.now(),
            serviceAliases = services.mapNotNull { it::class.qualifiedName }
        )
        send(frame = Frame.Text(actionResultJson.encodeToString(servicesPresentation)))
    }

    private suspend fun DefaultWebSocketServerSession.observeMessages() {
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val receivedText = frame.readText()
                try {
                    val action = Json.decodeFromString<ServiceAction<ServiceActionResult>>(receivedText)
                    services.act(action)?.let { result ->
                        try {
                            send(
                                Frame.Text(
                                    actionResultJson.encodeToString<ServiceActionResult>(
                                        result
                                    )
                                )
                            )
                        } catch (e: Throwable) {
                            Log.e(this::class.simpleName, "error on parsing action result", e)
                        }
                    }
                } catch (e: Throwable) {
                    Log.e(this::class.simpleName, "error on parsing action", e)
                }
            }
        }
        Log.e(this::class.simpleName, "ws session closed")
    }
}
