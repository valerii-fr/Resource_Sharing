package dev.nordix.services.domain.model

import dev.nordix.core.Constants
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data class ServiceContract(
    val type: ServiceType,
    val actions: List<KClass<*>>
) {

    enum class ServiceType(val typeName: String) {
        DEVICE_PROXY("_nordix_device_proxy${Constants.SERVICE_PROTOCOL}"),
        INFO("_nordix_info${Constants.SERVICE_PROTOCOL}"),
        NETWORK_PROXY("_nordix_network_proxy${Constants.SERVICE_PROTOCOL}"),
        SERVICE_PROXY("_nordix_service_proxy${Constants.SERVICE_PROTOCOL}"),
    }

}
