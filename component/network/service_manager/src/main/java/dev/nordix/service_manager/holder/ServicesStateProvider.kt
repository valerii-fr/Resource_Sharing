package dev.nordix.service_manager.holder

import android.net.nsd.NsdServiceInfo
import android.util.Log
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

    private val tag = this::class.simpleName

    fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
        Log.e(tag, "onStartDiscoveryFailed: serviceType = $serviceType, errorCode = $errorCode")
        update { state ->
            val serviceTypeIndex = state.discoveryStates.indexOfFirst { it.type == serviceType }
            if (serviceTypeIndex > -1) {
                val newDiscoveryState = state.discoveryStates[serviceTypeIndex]
                    .copy(state = DiscoveryState.State.Error)
                state.copy(
                    discoveryStates = state.discoveryStates.toMutableList().apply {
                        this[serviceTypeIndex] = newDiscoveryState }
                )
            } else state
        }
    }

    fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
        Log.e(tag, "onStopDiscoveryFailed: serviceType = $serviceType, errorCode = $errorCode")
        update { state ->
            val serviceTypeIndex = state.discoveryStates.indexOfFirst { it.type == serviceType }
            if (serviceTypeIndex > -1) {
                val newDiscoveryState = state.discoveryStates[serviceTypeIndex]
                    .copy(state = DiscoveryState.State.Error)
                state.copy(
                    discoveryStates = state.discoveryStates.toMutableList().apply {
                        this[serviceTypeIndex] = newDiscoveryState }
                )
            } else state
        }
    }

    fun onDiscoveryStarted(serviceType: String?) {
        Log.e(tag, "onDiscoveryStarted: serviceType = $serviceType")
        update { state ->
            val serviceTypeIndex = state.discoveryStates.indexOfFirst { it.type == serviceType }
            if (serviceTypeIndex > 1) {
                val newDiscoveryState = state.discoveryStates[serviceTypeIndex]
                    .copy(state = DiscoveryState.State.Active)
                state.copy(
                    discoveryStates = state.discoveryStates.toMutableList().apply {
                        this[serviceTypeIndex] = newDiscoveryState }
                )
            } else state
        }
    }

    fun onDiscoveryStopped(serviceType: String?) {
        Log.e(tag, "onDiscoveryStopped: serviceType = $serviceType")
        update { state ->
            val serviceTypeIndex = state.discoveryStates.indexOfFirst { it.type == serviceType }
            if (serviceTypeIndex > -1 ) {
                val newDiscoveryState = state.discoveryStates[serviceTypeIndex]
                    .copy(state = DiscoveryState.State.Stopped)
                state.copy(
                    discoveryStates = state.discoveryStates.toMutableList().apply {
                        this[serviceTypeIndex] = newDiscoveryState }
                )
            } else state
        }
    }

    fun onServiceFound(serviceInfo: NsdServiceInfo?) {
        Log.e(tag, "onServiceFound: serviceInfo = $serviceInfo")
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
        Log.e(tag, "onServiceLost: serviceInfo = $serviceInfo")
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