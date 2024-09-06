package dev.nordix.services.domain.model.actions

import dev.nordix.services.domain.model.results.FlashActionResult
import kotlinx.serialization.Serializable

@Serializable
sealed interface FlashAction <R: FlashActionResult> : ServiceAction<R> {
    @Serializable
    data object SwitchOn : FlashAction<FlashActionResult.SwitchedOn>
    @Serializable
    data object SwitchOff : FlashAction<FlashActionResult.SwitchedOff>
}
