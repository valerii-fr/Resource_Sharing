package dev.nordix.services

import dev.nordix.services.domain.model.ServiceDescriptor
import java.io.Serializable
import java.util.UUID

interface NordixService {
    val descriptor: ServiceDescriptor
    val netId get() = "${descriptor.owner.value}:${descriptor.name}/${descriptor.id.value}"
    val typeName get() = descriptor.contract.type.name + PROTOCOL_POSTFIX

    @JvmInline
    value class ID(val value: UUID) : Serializable {
        companion object {
            fun new() = ID(UUID.randomUUID())
        }
    }

    companion object {
        const val PROTOCOL_POSTFIX = ""
    }

}