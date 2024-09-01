package dev.nordix.service_manager.domain.model

data class LocalServiceState(
    val status: ServiceState.ServiceStatus,
    val serviceInfo: LocalServiceInfo,
) {

    val name get() = serviceInfo.name.substringAfter("/")

}
