package dev.nordix.publish.domain

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import dev.nordix.core.Constants
import dev.nordix.publish.ServicePublisher
import dev.nordix.publish.listeners.PublishingListener
import dev.nordix.service_manager.domain.model.ServiceInfo
import dev.nordix.service_manager.domain.model.mapper.toNsdService
import dev.nordix.service_manager.holder.ServicesStateProvider
import dev.nordix.settings.TerminalRepository
import javax.inject.Inject

class ServicePublisherImpl @Inject constructor(
    private val servicesStateProvider: ServicesStateProvider,
    private val terminalRepository: TerminalRepository,
    private val nsdManager: NsdManager,
) : ServicePublisher {

    private val servicesMap = mutableMapOf<String, PublishingListener>()

    override fun publishRootService() {
        val serviceInfo = NsdServiceInfo().apply {
            port = Constants.ROOT_SERVICE_PORT
            serviceName = terminalRepository.terminal.id.toRootServiceName()
            serviceType = Constants.ROOT_SERVICE_TYPE
        }
        nsdManager.registerService(
            serviceInfo,
            NsdManager.PROTOCOL_DNS_SD,
            servicesMap.getOrPut(serviceInfo.serviceName) {
                PublishingListener(servicesStateProvider)
            }
        )
    }

    override fun publishService(service: ServiceInfo) {
        nsdManager.registerService(
            service.toNsdService(),
            NsdManager.PROTOCOL_DNS_SD,
            servicesMap.getOrPut(service.name) {
                PublishingListener(servicesStateProvider)
            }
        )
    }

    override fun removeService(serviceName: String) {
        nsdManager.unregisterService(
            servicesMap.getOrPut(serviceName) {
                PublishingListener(servicesStateProvider)
            })
    }

    override fun removeAll() {
        servicesMap.keys.forEach(::removeService)
    }

}
