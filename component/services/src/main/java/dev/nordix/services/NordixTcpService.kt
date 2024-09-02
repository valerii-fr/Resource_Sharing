package dev.nordix.services

import dev.nordix.services.domain.model.ServiceAction
import dev.nordix.services.domain.model.ServiceActionResult
import dev.nordix.services.domain.model.ServiceDescriptor

interface NordixTcpService <R: ServiceActionResult, A: ServiceAction<R>> {

    val descriptor: ServiceDescriptor
    val namePath get() = "/${descriptor.name.replace(" ", "_")}"

    fun setupServer()
    fun terminate()
    suspend fun act(action: A) : R

}
