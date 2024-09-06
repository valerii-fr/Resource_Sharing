package dev.nordix.services.domain.model.results

import dev.nordix.services.impls.message_service.model.MessageEntity
import kotlinx.serialization.Serializable

@Serializable
sealed interface MessageActionResult : ServiceActionResult {

    @Serializable
    sealed interface SendResult : MessageActionResult {
        @Serializable
        data object Success : SendResult
        @Serializable
        data class Failure(val cause: String?) : SendResult
    }

    @Serializable
    sealed interface GetResult : MessageActionResult {
        @Serializable
        data class Success(val result: List<MessageEntity>) : GetResult
        @Serializable
        data class Failure(val cause: String?) : GetResult
    }

}
