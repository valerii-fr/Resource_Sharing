package dev.nordix.service_manager.domain.model

import java.net.InetAddress

data class ServiceInfo(
    val name: String,
    val deviceId: String,
    val type: String,
    val port: Int,
    val address: InetAddress?,
)
