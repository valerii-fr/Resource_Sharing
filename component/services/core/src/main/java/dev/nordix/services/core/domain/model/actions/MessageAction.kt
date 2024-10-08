package dev.nordix.services.domain.model.actions

import kotlinx.serialization.Serializable

@Serializable
sealed interface MessageAction <R: MessageActionResult> : ServiceAction<R> {

    @Serializable
    data class SendMessage(
        val address: String,
        val body: String,
    ) : MessageAction<MessageActionResult.SendResult>

    @Serializable
    data object GetMessages : MessageAction<MessageActionResult.GetResult>

}
