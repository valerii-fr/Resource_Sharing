package dev.nordix.core.model

import dev.nordix.core.Constants
import java.io.Serializable
import java.util.UUID
import kotlin.jvm.JvmInline

data class Terminal(
    val id: ID,
    val name: String,
) {

    @JvmInline
    value class ID(val value: UUID) : Serializable {
        fun toRootServiceName() : String = "$value/${Constants.ROOT_SERVICE_NAME}"
        companion object {
            fun new() = ID(UUID.randomUUID())
        }
    }

}
