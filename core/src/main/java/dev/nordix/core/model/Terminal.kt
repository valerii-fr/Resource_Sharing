package dev.nordix.core.model

import dev.nordix.core.Constants
import dev.nordix.core.utils.randomString
import java.io.Serializable
import java.util.UUID

data class Terminal(
    val id: ID,
    val name: String,
) {

    @JvmInline
    value class ID(val value: String) : Serializable {
        fun toRootServiceType() : String = "$value/${Constants.ROOT_DISCOVERY_TYPE}"
        companion object {
            fun new() = ID(randomString(6))
        }
    }

}
