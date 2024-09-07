package dev.nordix.discovery.listeners

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import dev.nordix.client_provider.domain.WssClientProvider
import dev.nordix.service_manager.holder.NsdServicesStateProvider
import dev.nordix.settings.TerminalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DiscoveryListener(
    private val nsdServicesStateProvider: NsdServicesStateProvider,
    private val nsdManager: NsdManager,
    private val wssClientProvider: WssClientProvider,
    private val scope: CoroutineScope,
    private val terminalRepository: TerminalRepository,
) : NsdManager.DiscoveryListener {

    override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) =
        nsdServicesStateProvider.onStartDiscoveryFailed(serviceType, errorCode)

    override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) =
        nsdServicesStateProvider.onStopDiscoveryFailed(serviceType, errorCode)

    override fun onDiscoveryStarted(serviceType: String?) =
        nsdServicesStateProvider.onDiscoveryStarted(serviceType)

    override fun onDiscoveryStopped(serviceType: String?) =
        nsdServicesStateProvider.onDiscoveryStopped(serviceType)

    override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
        nsdServicesStateProvider.onServiceFound(serviceInfo)
        nsdManager.resolveService(serviceInfo, ResolveListener(
            nsdServicesStateProvider = nsdServicesStateProvider,
            wssClientProvider = wssClientProvider,
            scope = scope,
            terminalRepository = terminalRepository,
        ))
    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
        nsdServicesStateProvider.onServiceLost(serviceInfo)
        scope.launch {
            wssClientProvider.terminateClient(serviceInfo?.host?.hostAddress ?: "")
        }
    }

}
