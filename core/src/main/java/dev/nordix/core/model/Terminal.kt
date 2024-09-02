package dev.nordix.core.model

import dev.nordix.core.Constants
import dev.nordix.core.utils.randomString
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
data class Terminal(
    val id: ID,
    val name: String,
) {

    @Serializable
    @JvmInline
    value class ID(@Contextual val value: String) {
        fun toRootServiceName() : String = "$value/${Constants.ROOT_SERVICE_NAME}"
        companion object {
            fun new() = ID(randomString(8))
        }
    }

}
