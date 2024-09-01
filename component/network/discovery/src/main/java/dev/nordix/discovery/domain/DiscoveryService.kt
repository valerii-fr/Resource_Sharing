package dev.nordix.discovery.domain

import dev.nordix.service_manager.domain.model.ServiceInfo
import kotlinx.coroutines.flow.StateFlow

interface DiscoveryService {

    val foundServices: StateFlow<List<ServiceInfo>>

    fun startRootServiceLookup()

}
