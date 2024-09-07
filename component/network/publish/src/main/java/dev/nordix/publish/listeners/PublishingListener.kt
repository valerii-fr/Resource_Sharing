package dev.nordix.publish.listeners

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import dev.nordix.service_manager.holder.NsdServicesStateProvider
import javax.inject.Inject

class PublishingListener @Inject constructor(
    private val nsdServicesStateProvider: NsdServicesStateProvider,
) : NsdManager.RegistrationListener {

    override fun onRegistrationFailed(
        serviceInfo: NsdServiceInfo?,
        errorCode: Int
    ) = nsdServicesStateProvider.onRegistrationFailed(serviceInfo, errorCode)

    override fun onUnregistrationFailed(
        serviceInfo: NsdServiceInfo?,
        errorCode: Int
    ) = nsdServicesStateProvider.onUnregistrationFailed(serviceInfo, errorCode)

    override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) =
        nsdServicesStateProvider.onServiceRegistered(serviceInfo)

    override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) =
        nsdServicesStateProvider.onServiceUnregistered(serviceInfo)

}
