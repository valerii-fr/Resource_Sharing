package dev.nordix.discovery.listeners

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import dev.nordix.service_manager.holder.ServicesStateProvider
import javax.inject.Inject

class ResolveListener @Inject constructor(
    private val servicesStateProvider: ServicesStateProvider,
) : NsdManager.ResolveListener {

    override fun onResolveFailed(
        serviceInfo: NsdServiceInfo?,
        errorCode: Int
    ) = servicesStateProvider.onResolveFailed(serviceInfo, errorCode)

    override fun onServiceResolved(serviceInfo: NsdServiceInfo?) =
        servicesStateProvider.onServiceResolved(serviceInfo)

}
