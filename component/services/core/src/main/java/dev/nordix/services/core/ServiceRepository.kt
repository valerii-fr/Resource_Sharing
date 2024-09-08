package dev.nordix.services

import kotlinx.coroutines.flow.Flow

interface ServiceRepository {

    fun observeServices() : Flow<List<NordixTcpService<*,*>>>
    fun addService(service: NordixTcpService<*,*>)

}
