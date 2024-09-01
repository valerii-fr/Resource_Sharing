package dev.nordix.service_manager.domain.model

data class FoundServiceState(
    val status: ServiceState.ServiceStatus,
    val serviceInfo: FoundServiceInfo,
) {

    val name get() = serviceInfo.name.substringAfterLast(":").substringBefore("/")

}
