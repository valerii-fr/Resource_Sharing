package dev.nordix.services.domain

import dev.nordix.services.domain.model.actions.MessageAction.GetMessages
import dev.nordix.services.domain.model.actions.MessageAction.SendMessage
import dev.nordix.services.domain.model.actions.ServiceAction
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

object ActionSerializer {

    val messageActionModule = SerializersModule {
        polymorphic(ServiceAction::class) {
            subclass(SendMessage::class, SendMessage.serializer())
            subclass(GetMessages::class, GetMessages.serializer())
        }
    }

    val messageActionJson = Json {
        serializersModule = messageActionModule
        prettyPrint = true
        encodeDefaults = true
    }

}
