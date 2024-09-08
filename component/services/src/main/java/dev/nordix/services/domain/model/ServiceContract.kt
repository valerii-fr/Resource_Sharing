package dev.nordix.services.domain.model

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data class ServiceContract(
    val type: ServiceType,
    val actions: List<KClass<*>>
) {

    enum class ServiceType() {
        DEVICE_PROXY,
        INFO,
        SERVICE_PROXY,
    }

}
