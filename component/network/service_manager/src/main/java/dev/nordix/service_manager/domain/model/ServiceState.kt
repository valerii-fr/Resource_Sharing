package dev.nordix.service_manager.domain.model

data class ServiceState(
    val status: ServiceStatus,
    val serviceInfo: ServiceInfo,
) {

    val name get() = serviceInfo.name.substringAfter("/")

    enum class ServiceStatus {
        Found,
        RegistrationFailed,
        UnregistrationFailed,
        Registered,
        Unregistered,
        Resolved,
        ResolveFailed,
    }

}
