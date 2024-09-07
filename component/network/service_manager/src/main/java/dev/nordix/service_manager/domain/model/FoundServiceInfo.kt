package dev.nordix.service_manager.domain.model

data class FoundServiceInfo(
    override val name: String,
    val deviceId: String,
    override val type: String,
    override val port: Int = 0,
) : ServiceInfo
