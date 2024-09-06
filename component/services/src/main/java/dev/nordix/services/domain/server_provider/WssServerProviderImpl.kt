package dev.nordix.services.domain.server_provider

import android.util.Log
import dev.nordix.core.utils.getGenericTypesOf
import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.ActionSerializer.actionJson
import dev.nordix.services.domain.ActionSerializer.actionResultJson
import dev.nordix.services.domain.WssServerProvider
import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.results.ServiceActionResult
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
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes

class WssServerProviderImpl @Inject constructor(
    private val services: Set<@JvmSuppressWildcards NordixTcpService<*, *>>
) : WssServerProvider {

    override fun getServer(): NettyApplicationEngine {

        val environment = applicationEngineEnvironment {
            connector {
                port = 21337
                host = "0.0.0.0"
            }

            module {
                install(WebSockets)
                routing {
                    webSocket("/ws") {
                        try {
                            val s = actionJson.encodeToString<ServiceAction<ServiceActionResult>>(
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
                                    services.act(action)?.let { result ->
                                        try {
                                            send(Frame.Text(actionResultJson.encodeToString<ServiceActionResult>(result)))
                                        } catch (e: Throwable) {
                                            Log.e(this::class.simpleName, "error on parsing action result", e)
                                        }
                                    }
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
                Log.i(this::class.simpleName, "Server started at ${connector.host}:${connector.port}")
            }
        }
    }

    private suspend fun Set<@JvmSuppressWildcards NordixTcpService<*, *>>.act(
        action: ServiceAction<ServiceActionResult>
    ) : ServiceActionResult? {
        val requiredTypes = action::class.allSupertypes.map { it.classifier }.filter { it is KClass<*> }
        return firstOrNull { service ->
            service.getGenericTypesOf(NordixTcpService::class).first() in requiredTypes
        }
            ?.let { service ->
                service.typify().act(action)
            } ?: run {
                Log.e(this::class.simpleName, "service not found for action $action")
                null
            }
    }

    inline fun <reified S : NordixTcpService<*, *>> S.typify(
    ): NordixTcpService<ServiceAction<ServiceActionResult>, ServiceActionResult> {
        return try {
            @Suppress("UNCHECKED_CAST")
            this as NordixTcpService<ServiceAction<ServiceActionResult>, ServiceActionResult>
        } catch (_: Throwable) {
            throw UnsupportedOperationException("Cannot proceed with $this")
        }
    }
}