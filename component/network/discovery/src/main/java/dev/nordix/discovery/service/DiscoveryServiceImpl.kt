package dev.nordix.discovery.service

import android.net.nsd.NsdManager
import android.util.Log
import dev.nordix.discovery.domain.DiscoveryService
import dev.nordix.core.Constants
import dev.nordix.discovery.listeners.DiscoveryListener
import dev.nordix.service_manager.domain.model.FoundServiceInfo
import dev.nordix.service_manager.domain.model.ServiceInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class DiscoveryServiceImpl @Inject constructor(
    scope: CoroutineScope,
    private val nsdManager: NsdManager,
    private val discoveryListener: DiscoveryListener,
) : DiscoveryService {


    private val tag = this::class.simpleName

    init {
        Log.i(tag, "Discovery Service was initialized")
    }

    override val foundServices: StateFlow<List<FoundServiceInfo>> =
        discoveryListener.foundDevices.map { it.foundServiceStates.map { it.serviceInfo } }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override val resolvedServices: StateFlow<List<ServiceInfo>> =
        discoveryListener.foundDevices.map { it.resolvedServiceStates.map { it.serviceInfo } }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override fun startRootServiceLookup() {
        nsdManager.discoverServices(
            Constants.ROOT_SERVICE_TYPE,
            NsdManager.PROTOCOL_DNS_SD,
            discoveryListener
        )
    }

}
