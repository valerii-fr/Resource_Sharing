package dev.nordix.discovery.service

import android.net.nsd.NsdManager
import dev.nordix.client_provider.domain.WssClientProvider
import dev.nordix.discovery.domain.DiscoveryService
import dev.nordix.core.Constants
import dev.nordix.discovery.listeners.DiscoveryListener
import dev.nordix.service_manager.domain.model.FoundServiceInfo
import dev.nordix.service_manager.domain.model.ServiceInfo
import dev.nordix.service_manager.holder.NsdServicesStateProvider
import dev.nordix.settings.TerminalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class DiscoveryServiceImpl @Inject constructor(
    private val scope: CoroutineScope,
    private val nsdManager: NsdManager,
    private val nsdServicesStateProvider: NsdServicesStateProvider,
    private val wssClientProvider: WssClientProvider,
    private val terminalRepository: TerminalRepository,
) : DiscoveryService {

    private val registeredListeners = mutableMapOf<String, DiscoveryListener>()

    override val foundServices: StateFlow<List<FoundServiceInfo>> =
        nsdServicesStateProvider.map { it.foundServiceStates.map { it.serviceInfo } }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override val resolvedServices: StateFlow<List<ServiceInfo>> =
        nsdServicesStateProvider.map { it.resolvedServiceStates.map { it.serviceInfo } }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override fun startLookup() {
        nsdManager.discoverServices(
            Constants.ROOT_SERVICE_TYPE,
            NsdManager.PROTOCOL_DNS_SD,
            registeredListeners.getOrPut(Constants.ROOT_SERVICE_TYPE) {
                DiscoveryListener(
                    nsdServicesStateProvider = nsdServicesStateProvider,
                    nsdManager = nsdManager,
                    wssClientProvider = wssClientProvider,
                    scope = scope,
                    terminalRepository = terminalRepository,
                )
            }
        )
    }

    override fun stopDiscovery() {
        registeredListeners.values.forEach(nsdManager::stopServiceDiscovery)
    }

}
