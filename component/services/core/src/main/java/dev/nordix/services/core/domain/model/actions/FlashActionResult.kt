package dev.nordix.services.domain.model.actions

import kotlinx.serialization.Serializable

@Serializable
sealed interface FlashActionResult : ServiceActionResult {

    @Serializable
    data object SwitchedOn : FlashActionResult

    @Serializable
    data object SwitchedOff : FlashActionResult

    @Serializable
    data class Failure(val cause: String?) : FlashActionResult

}
