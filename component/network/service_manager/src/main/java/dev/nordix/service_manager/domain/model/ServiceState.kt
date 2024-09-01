package dev.nordix.service_manager.domain.model

data class ServiceState(
    val status: ServiceStatus,
    val serviceInfo: ServiceInfo,
) {

    val name get() = serviceInfo.name.substringAfterLast(":").substringBefore("/")

    enum class ServiceStatus {
        Active,
        Found,
        Error,
        Stopper,
        RegistrationFailed,
        UnregistrationFailed,
        Registered,
    }

}
