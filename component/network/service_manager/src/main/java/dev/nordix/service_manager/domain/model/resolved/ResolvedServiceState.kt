package dev.nordix.service_manager.domain.model.resolved

import dev.nordix.service_manager.domain.model.ServiceState
import dev.nordix.service_manager.domain.model.ServiceState.ServiceStatus

data class ResolvedServiceState(
    override val status: ServiceStatus,
    override val serviceInfo: ResolvedServiceInfo,
) : ServiceState {
    val terminalId get() = serviceInfo.deviceId
}
