package dev.nordix.services

import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.actions.ServiceActionResult
import dev.nordix.services.domain.model.ServiceDescriptor
import dev.nordix.services.domain.model.actions.FlashAction
import dev.nordix.services.domain.model.actions.FlashActionResult
import dev.nordix.services.domain.model.actions.MessageAction
import dev.nordix.services.domain.model.actions.MessageActionResult
import dev.nordix.services.domain.model.actions.ServicesPresentationAction
import dev.nordix.services.domain.model.actions.ServicesPresentationResult

sealed interface NordixTcpService <A: ServiceAction<R>, R: ServiceActionResult> {

    val descriptor: ServiceDescriptor
    fun terminate()
    suspend fun act(action: A) : R

    abstract class SmsService() : NordixTcpService<MessageAction<MessageActionResult>, MessageActionResult>
    abstract class FlashService() : NordixTcpService<FlashAction<FlashActionResult>, FlashActionResult>
    abstract class PresentationService() : NordixTcpService<ServicesPresentationAction<ServicesPresentationResult>, ServicesPresentationResult>

}
