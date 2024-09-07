package dev.nordix.services.domain.model.actions

import kotlinx.serialization.Serializable

@Serializable
sealed interface ServicesPresentationResult : ServiceActionResult {

    sealed interface ServerPresentationResult : ServicesPresentationResult {

        @Serializable
        data object Success : ServerPresentationResult

        @Serializable
        data class Error(val message: String?) : ServerPresentationResult
    }

    sealed interface ClientPresentationResult : ServicesPresentationResult {

        @Serializable
        data object Success : ClientPresentationResult

        @Serializable
        data class Error(val message: String?) : ClientPresentationResult
    }

}
