package dev.nordix.publish.listeners

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import dev.nordix.service_manager.holder.ServicesStateProvider
import javax.inject.Inject

class PublishingListener @Inject constructor(
    private val servicesStateProvider: ServicesStateProvider,
) : NsdManager.RegistrationListener {

    override fun onRegistrationFailed(
        serviceInfo: NsdServiceInfo?,
        errorCode: Int
    ) = servicesStateProvider.onRegistrationFailed(serviceInfo, errorCode)

    override fun onUnregistrationFailed(
        serviceInfo: NsdServiceInfo?,
        errorCode: Int
    ) = servicesStateProvider.onUnregistrationFailed(serviceInfo, errorCode)

    override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) =
        servicesStateProvider.onServiceRegistered(serviceInfo)

    override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) =
        servicesStateProvider.onServiceUnregistered(serviceInfo)

}
