package dev.nordix.service_manager.domain.model

import dev.nordix.service_manager.domain.model.ServiceState.ServiceStatus
import dev.nordix.service_manager.domain.model.found.LocalServiceInfo

data class LocalServiceState(
    override val status: ServiceStatus,
    override val serviceInfo: LocalServiceInfo,
) : ServiceState
