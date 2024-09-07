package dev.nordix.service_manager.domain.model

interface ServiceInfo {
    val name: String
    val type: String
    val port: Int
}
