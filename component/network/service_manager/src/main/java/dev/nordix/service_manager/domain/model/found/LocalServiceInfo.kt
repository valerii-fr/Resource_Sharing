package dev.nordix.service_manager.domain.model.found

import dev.nordix.service_manager.domain.model.ServiceInfo

data class LocalServiceInfo(
    override val name: String,
    override val type: String,
    override val port: Int,
) : ServiceInfo
