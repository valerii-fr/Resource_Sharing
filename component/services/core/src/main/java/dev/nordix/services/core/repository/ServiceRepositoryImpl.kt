package dev.nordix.services.repository

import dev.nordix.services.NordixTcpService
import dev.nordix.services.ServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    services: Set<@JvmSuppressWildcards NordixTcpService<*,*>>
) : ServiceRepository {

    val localServices: StateFlow<List<NordixTcpService<*,*>>>
        field = MutableStateFlow(services.toList())

    override fun observeServices(): Flow<List<NordixTcpService<*,*>>> = localServices

    override fun addService(service: NordixTcpService<*,*>) {
        localServices.update { services ->
            services + service
        }
    }

}
