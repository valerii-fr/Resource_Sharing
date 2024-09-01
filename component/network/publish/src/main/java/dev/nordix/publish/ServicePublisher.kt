package dev.nordix.publish

import dev.nordix.service_manager.domain.model.ServiceInfo

interface ServicePublisher {

    fun publishRootService()
    fun publishService(service: ServiceInfo)
    fun removeService(serviceName: String)
    fun removeAll()

}
