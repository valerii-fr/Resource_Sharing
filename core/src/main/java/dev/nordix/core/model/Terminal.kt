package dev.nordix.core.model

import dev.nordix.core.Constants
import dev.nordix.core.utils.randomString
import java.io.Serializable
import kotlin.jvm.JvmInline

data class Terminal(
    val id: ID,
    val name: String,
) {

    @JvmInline
    value class ID(val value: String) : Serializable {
        fun toRootServiceName() : String = "$value/${Constants.ROOT_SERVICE_NAME}"
        companion object {
            fun new() = ID(randomString(8))
        }
    }

}
