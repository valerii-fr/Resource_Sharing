package dev.nordix.publish

interface ServicePublisher {

    fun publishRootService()
    fun removeService(serviceName: String)
    fun removeAll()

}
