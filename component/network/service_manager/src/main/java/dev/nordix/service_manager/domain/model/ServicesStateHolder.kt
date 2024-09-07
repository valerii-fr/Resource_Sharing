package dev.nordix.service_manager.domain.model

import dev.nordix.service_manager.domain.model.found.FoundServiceState
import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceState
import java.time.Instant

data class ServicesStateHolder(
    val localServiceStates: List<LocalServiceState> = emptyList<LocalServiceState>(),
    val foundServiceStates: List<FoundServiceState> = emptyList<FoundServiceState>(),
    val resolvedServiceStates: List<ResolvedServiceState> = emptyList<ResolvedServiceState>(),
    val discoveryStates: List<DiscoveryState> = emptyList<DiscoveryState>(),
    val updated: Instant = Instant.now()
)
