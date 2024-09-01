package dev.nordix.services.domain.model

import dev.nordix.core.model.Terminal
import java.io.Serializable
import java.util.UUID

data class ServiceDescriptor(
    val name: String,
    val owner: Terminal.ID,
    val port: Int,
    val id: ID = ID.new(),
    val contract: ServiceContract,
    val services: List<ServiceDescriptor>,
) {

    @JvmInline
    value class ID(val value: UUID) : Serializable {
        companion object {
            fun new() = ID(UUID.randomUUID())
        }
    }

}
