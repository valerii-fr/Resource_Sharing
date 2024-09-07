package dev.nordix.service_manager.domain.model.found

import dev.nordix.service_manager.domain.model.FoundServiceInfo
import dev.nordix.service_manager.domain.model.ServiceState
import dev.nordix.service_manager.domain.model.ServiceState.ServiceStatus

data class FoundServiceState(
    override val status: ServiceStatus,
    override val serviceInfo: FoundServiceInfo,
) : ServiceState