package dev.nordix.services.impls.message_service.model

import dev.nordix.services.domain.model.ServiceAction
import kotlinx.serialization.Serializable

@Serializable
sealed interface MessageAction <R: MessageActionResult> : ServiceAction<R> {

    override val identifier: String? get() = this::class.simpleName

    @Serializable
    data class SendMessage(
        override val identifier: String? = this::class.qualifiedName,
        val address: String,
        val body: String,
    ) : MessageAction<MessageActionResult.SendResult>

    @Serializable
    data object GetMessages : MessageAction<MessageActionResult.GetResult> {
        override val identifier: String? = this::class.simpleName
    }

}