package dev.nordix.service_manager.holder

import android.net.nsd.NsdServiceInfo
import dev.nordix.service_manager.domain.model.DiscoveryState
import dev.nordix.service_manager.domain.model.ServiceState
import dev.nordix.service_manager.domain.model.ServicesStateHolder
import dev.nordix.service_manager.domain.model.toFoundServiceInfo
import dev.nordix.service_manager.domain.model.toServiceInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ServicesStateProvider(
    val holder: MutableStateFlow<ServicesStateHolder> = MutableStateFlow(ServicesStateHolder())
) : MutableStateFlow<ServicesStateHolder> by holder {

    fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
        update { state ->
            val serviceTypeIndex = state.discoveryStates.indexOfFirst { it.type == serviceType }
            val newDiscoveryState = state.discoveryStates[serviceTypeIndex]
                .copy(state = DiscoveryState.State.Error)
            state.copy(
                discoveryStates = state.discoveryStates.toMutableList().apply {
                    this[serviceTypeIndex] = newDiscoveryState }
            )
        }
    }

    fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
        update { state ->
            val serviceTypeIndex = state.discoveryStates.indexOfFirst { it.type == serviceType }
            val newDiscoveryState = state.discoveryStates[serviceTypeIndex]
                .copy(state = DiscoveryState.State.Error)
            state.copy(
                discoveryStates = state.discoveryStates.toMutableList().apply {
                    this[serviceTypeIndex] = newDiscoveryState }
            )
        }
    }

    fun onDiscoveryStarted(serviceType: String?) {
        update { state ->
            val serviceTypeIndex = state.discoveryStates.indexOfFirst { it.type == serviceType }
            val newDiscoveryState = state.discoveryStates[serviceTypeIndex]
                .copy(state = DiscoveryState.State.Active)
            state.copy(
                discoveryStates = state.discoveryStates.toMutableList().apply {
                    this[serviceTypeIndex] = newDiscoveryState }
            )
        }
    }

    fun onDiscoveryStopped(serviceType: String?) {
        update { state ->
            val serviceTypeIndex = state.discoveryStates.indexOfFirst { it.type == serviceType }
            val newDiscoveryState = state.discoveryStates[serviceTypeIndex]
                .copy(state = DiscoveryState.State.Stopped)
            state.copy(
                discoveryStates = state.discoveryStates.toMutableList().apply {
                    this[serviceTypeIndex] = newDiscoveryState }
            )
        }
    }

    fun onServiceFound(serviceInfo: NsdServiceInfo?) {
        serviceInfo?.let {
            update { state ->
                state.copy(
                    serviceStates = state.serviceStates.toMutableList().apply {
                        add(
                            ServiceState(
                                status = ServiceState.ServiceStatus.Found,
                                serviceInfo = serviceInfo.toServiceInfo()
                            )
                        )
                    }
                )
            }
        }
    }

    fun onServiceLost(serviceInfo: NsdServiceInfo?) {
        serviceInfo?.let {
            val domainServiceInfo = serviceInfo.toFoundServiceInfo()
            update { state ->
                state.copy(
                    serviceStates = state.serviceStates.toMutableList().apply {
                        removeAll { it.serviceInfo == domainServiceInfo }
                    }
                )
            }
        }
    }

}