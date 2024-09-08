package dev.nordix.services.domain

import dev.nordix.services.domain.model.actions.ServiceActionResult
import dev.nordix.services.domain.model.actions.MessageAction.GetMessages
import dev.nordix.services.domain.model.actions.MessageAction.SendMessage
import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.actions.MessageActionResult
import dev.nordix.services.domain.model.actions.ServiceInteraction
import dev.nordix.services.domain.model.actions.ServicesPresentationResult
import dev.nordix.services.domain.model.actions.ServicesPresentationResult.ClientPresentationResult
import dev.nordix.services.domain.model.actions.ServicesPresentationResult.ServerPresentationResult
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

object ActionSerializer {

    val serviceInteractionModule = SerializersModule {

        polymorphic(ServiceInteraction::class) {
            polymorphic(ServiceAction::class) {
                subclass(SendMessage::class, SendMessage.serializer())
                subclass(GetMessages::class, GetMessages.serializer())
            }

            polymorphic(ServiceActionResult::class) {
                polymorphic(ServicesPresentationResult::class) {
                    polymorphic(ClientPresentationResult::class) {
                        subclass(ClientPresentationResult.Success::class, ClientPresentationResult.Success.serializer())
                        subclass(ClientPresentationResult.Error::class, ClientPresentationResult.Error.serializer())
                    }
                    polymorphic(ServerPresentationResult::class) {
                        subclass(ServerPresentationResult.Success::class, ServerPresentationResult.Success.serializer())
                        subclass(ServerPresentationResult.Error::class, ServerPresentationResult.Error.serializer())
                    }
                }
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
    }

    val serviceInteractionJson = Json {
        serializersModule = serviceInteractionModule
        prettyPrint = true
        encodeDefaults = true
    }

}
