package dev.nordix.service_manager.domain.model

data class LocalServiceInfo(
    val name: String,
    val type: String,
    val port: Int,
)
