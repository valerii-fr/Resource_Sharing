package dev.nordix.core.model

import java.io.Serializable
import java.util.UUID

data class Terminal(
    val id: ID,
) {

    @JvmInline
    value class ID(val value: UUID) : Serializable {
        companion object {
            fun new() = ID(UUID.randomUUID())
        }
    }

}
