package dev.nordix.services.domain.model

interface ServiceContract {

    val type: ServiceType
    val actions: List<ServiceAction>

    enum class ServiceType {
        DEVICE_PROXY,
        NETWORK_PROXY,
    }

}
