package dev.nordix.discovery.domain

import dev.nordix.service_manager.domain.model.FoundServiceInfo
import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceInfo
import kotlinx.coroutines.flow.StateFlow

interface DiscoveryService {

    val foundServices: StateFlow<List<FoundServiceInfo>>
    val resolvedServices: StateFlow<List<ResolvedServiceInfo>>

    fun startLookup()
    fun stopDiscovery()

}
