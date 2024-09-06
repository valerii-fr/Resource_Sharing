package dev.nordix.services

import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.results.ServiceActionResult
import dev.nordix.services.domain.model.ServiceDescriptor

interface NordixTcpService <A: ServiceAction<R>, R: ServiceActionResult> {

    val descriptor: ServiceDescriptor
    fun terminate()
    suspend fun act(action: A) : R

}
