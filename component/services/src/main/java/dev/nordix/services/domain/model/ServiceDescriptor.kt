package dev.nordix.services.domain.model

import dev.nordix.core.model.Terminal
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ServiceDescriptor(
    val name: String,
    @Contextual
    val owner: Terminal.ID,
    val port: Int,
    val id: ID = ID.new(),
    val contract: ServiceContract,
    val services: List<ServiceDescriptor> = emptyList(),
) {

    val netId get() = "${owner.value}:${name}/${id.value}"
    val typeName get() = contract.type.typeName

    @Serializable
    @JvmInline
    value class ID(@Contextual val value: UUID) {
        companion object {
            fun new() = ID(UUID.randomUUID())
        }
    }

}
