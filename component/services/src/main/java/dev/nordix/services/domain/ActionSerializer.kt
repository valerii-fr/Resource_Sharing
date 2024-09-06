package dev.nordix.services.domain

import dev.nordix.services.domain.model.results.ServiceActionResult
import dev.nordix.services.domain.model.actions.MessageAction.GetMessages
import dev.nordix.services.domain.model.actions.MessageAction.SendMessage
import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.results.MessageActionResult
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

object ActionSerializer {

    val actionModule = SerializersModule {
        polymorphic(ServiceAction::class) {
            subclass(SendMessage::class, SendMessage.serializer())
            subclass(GetMessages::class, GetMessages.serializer())
        }
    }

    val actionResultModule = SerializersModule {
        polymorphic(ServiceActionResult::class) {
            polymorphic(MessageActionResult::class) {
                polymorphic(MessageActionResult.SendResult::class) {
                    subclass(MessageActionResult.SendResult.Success::class, MessageActionResult.SendResult.Success.serializer())
                    subclass(MessageActionResult.SendResult.Failure::class, MessageActionResult.SendResult.Failure.serializer())
                }
                polymorphic(MessageActionResult.GetResult::class) {
                    subclass(MessageActionResult.GetResult.Success::class, MessageActionResult.GetResult.Success.serializer())
                    subclass(MessageActionResult.GetResult.Failure::class, MessageActionResult.GetResult.Failure.serializer())
                }
            }
        }
    }

    val actionJson = Json {
        serializersModule = actionModule
        prettyPrint = true
        encodeDefaults = true
    }

    val actionResultJson = Json {
        serializersModule = actionResultModule
        prettyPrint = true
        encodeDefaults = true
    }

}
