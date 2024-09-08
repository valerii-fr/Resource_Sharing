package dev.nordix.service_manager.domain.model.resolved

import dev.nordix.service_manager.domain.model.ServiceInfo
import java.net.InetAddress

data class ResolvedServiceInfo(
    override val name: String,
    override val type: String,
    override val port: Int,
    val deviceId: String,
    val address: InetAddress?,
    val serviceAliases: List<String> = emptyList(),
    val knownDevices: List<String> = emptyList(),
) : ServiceInfo
