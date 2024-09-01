package dev.nordix.publish.domain

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import dev.nordix.core.Constants
import dev.nordix.publish.ServicePublisher
import dev.nordix.publish.listeners.PublishingListener
import dev.nordix.settings.TerminalRepository
import javax.inject.Inject

class ServicePublisherImpl @Inject constructor(
    private val terminalRepository: TerminalRepository,
    private val nsdManager: NsdManager,
    private val publishingListener: PublishingListener,
) : ServicePublisher {

    override fun publishRootService() {
        val serviceInfo = NsdServiceInfo().apply {
            port = 31337
            serviceName = Constants.ROOT_SERVICE_NAME
            serviceType = terminalRepository.getTerminal().id.toRootServiceType()
        }
        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, publishingListener)
    }

}
