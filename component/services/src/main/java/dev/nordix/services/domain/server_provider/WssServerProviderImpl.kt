package dev.nordix.services.domain.server_provider

import android.util.Log
import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.ActionSerializer.messageActionJson
import dev.nordix.services.domain.WssServerProvider
import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.ServiceActionResult
import dev.nordix.services.domain.model.actions.MessageAction.SendMessage
import io.ktor.server.application.install
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class WssServerProviderImpl @Inject constructor() : WssServerProvider {

    override fun getServer(service: NordixTcpService<*, *>): NettyApplicationEngine {

        val environment = applicationEngineEnvironment {
            connector {
                port = service.descriptor.port
                host = "0.0.0.0"
            }

            val path = "${service.namePath}/ws"

            module {
                install(WebSockets)
                routing {
                    webSocket(path) {
                        try {
                            val s = messageActionJson.encodeToString<ServiceAction<ServiceActionResult>>(
                                SendMessage(address = "123", body = "Hello World")
                            )
                            send(Frame.Text(s))
                        } catch (e: Throwable) {
                            Log.e(this::class.simpleName, "error on parsing action", e)
                        }
                        for (frame in incoming) {
                            if (frame is Frame.Text) {
                                val receivedText = frame.readText()
                                try {
                                    val action = Json.decodeFromString<ServiceAction<ServiceActionResult>>(receivedText)
                                    service.typify().act(action)
                                    send(Frame.Text("You said: $receivedText"))
                                } catch (e: Throwable) {
                                    Log.e(this::class.simpleName, "error on parsing action", e)
                                }
                            }
                        }
                    }
                }
            }
        }

        return embeddedServer(Netty, environment).apply {
            this.environment.connectors.forEach { connector ->
                Log.i(this::class.simpleName, "Server ${service.namePath} started at ${connector.host}:${connector.port}")
            }
        }
    }

    inline fun <reified S : NordixTcpService<*, *>> S.typify(
    ): NordixTcpService<ServiceActionResult, ServiceAction<ServiceActionResult>> {
        return try {
            @Suppress("UNCHECKED_CAST")
            this as NordixTcpService<ServiceActionResult, ServiceAction<ServiceActionResult>>
        } catch (_: Throwable) {
            throw UnsupportedOperationException("Cannot proceed with $this")
        }
    }
}