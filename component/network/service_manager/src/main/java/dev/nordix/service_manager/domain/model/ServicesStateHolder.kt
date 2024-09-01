package dev.nordix.service_manager.domain.model

import java.time.Instant

data class ServicesStateHolder(
    val localServiceStates: List<ServiceState> = emptyList<ServiceState>(),
    val remoteServiceStates: List<ServiceState> = emptyList<ServiceState>(),
    val discoveryStates: List<DiscoveryState> = emptyList<DiscoveryState>(),
    val updated: Instant = Instant.now()
)
