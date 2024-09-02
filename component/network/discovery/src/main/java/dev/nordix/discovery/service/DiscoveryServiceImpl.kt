package dev.nordix.discovery.service

import android.net.nsd.NsdManager
import android.util.Log
import dev.nordix.discovery.domain.DiscoveryService
import dev.nordix.core.Constants
import dev.nordix.discovery.listeners.DiscoveryListener
import dev.nordix.service_manager.domain.model.FoundServiceInfo
import dev.nordix.service_manager.domain.model.ServiceInfo
import dev.nordix.service_manager.holder.ServicesStateProvider
import dev.nordix.services.domain.model.ServiceContract
import dev.nordix.services.domain.model.ServiceDescriptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class DiscoveryServiceImpl @Inject constructor(
    scope: CoroutineScope,
    private val nsdManager: NsdManager,
    private val servicesStateProvider: ServicesStateProvider,
) : DiscoveryService {

    private val registeredListeners = mutableMapOf<String, DiscoveryListener>()

    override val foundServices: StateFlow<List<FoundServiceInfo>> =
        servicesStateProvider.map { it.foundServiceStates.map { it.serviceInfo } }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override val resolvedServices: StateFlow<List<ServiceInfo>> =
        servicesStateProvider.map { it.resolvedServiceStates.map { it.serviceInfo } }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override fun startLookup() {
        nsdManager.discoverServices(
            Constants.ROOT_SERVICE_TYPE,
            NsdManager.PROTOCOL_DNS_SD,
            registeredListeners.getOrPut(Constants.ROOT_SERVICE_TYPE) {
                DiscoveryListener(servicesStateProvider, nsdManager)
            }
        )
    }

    override fun stopDiscovery() {
        registeredListeners.values.forEach(nsdManager::stopServiceDiscovery)
    }

}
