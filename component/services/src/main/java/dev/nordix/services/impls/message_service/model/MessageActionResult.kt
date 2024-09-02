package dev.nordix.services.impls.message_service.model

import dev.nordix.services.domain.model.ServiceActionResult

sealed interface MessageActionResult : ServiceActionResult {

    sealed interface SendResult : MessageActionResult {
        data object Success : SendResult
        data class Failure(val cause: String?) : SendResult
    }

    sealed interface GetResult : MessageActionResult {
        data class Success(val result: List<MessageEntity>) : GetResult
        data class Failure(val cause: String?) : GetResult
    }

}
