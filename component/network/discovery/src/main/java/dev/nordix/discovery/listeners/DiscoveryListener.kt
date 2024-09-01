package dev.nordix.discovery.listeners

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import dev.nordix.service_manager.domain.model.ServicesStateHolder
import dev.nordix.service_manager.holder.ServicesStateProvider
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DiscoveryListener @Inject constructor(
    private val servicesStateProvider: ServicesStateProvider,
    private val nsdManager: NsdManager,
    private val resolveListener: ResolveListener,
) : NsdManager.DiscoveryListener {

    val foundDevices: StateFlow<ServicesStateHolder> = servicesStateProvider

    override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) =
        servicesStateProvider.onStartDiscoveryFailed(serviceType, errorCode)

    override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) =
        servicesStateProvider.onStopDiscoveryFailed(serviceType, errorCode)

    override fun onDiscoveryStarted(serviceType: String?) =
        servicesStateProvider.onDiscoveryStarted(serviceType)

    override fun onDiscoveryStopped(serviceType: String?) =
        servicesStateProvider.onDiscoveryStopped(serviceType)

    override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
        servicesStateProvider.onServiceFound(serviceInfo)
        nsdManager.resolveService(serviceInfo, resolveListener)
    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo?) =
        servicesStateProvider.onServiceLost(serviceInfo)

}
