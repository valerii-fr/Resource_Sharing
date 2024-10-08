package dev.nordix.service_manager.holder

import android.net.nsd.NsdServiceInfo
import android.util.Log
import dev.nordix.service_manager.domain.model.DiscoveryState
import dev.nordix.service_manager.domain.model.LocalServiceState
import dev.nordix.service_manager.domain.model.ServiceState.ServiceStatus
import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceState
import dev.nordix.service_manager.domain.model.ServicesStateHolder
import dev.nordix.service_manager.domain.model.found.FoundServiceState
import dev.nordix.service_manager.domain.model.mapper.terminalId
import dev.nordix.service_manager.domain.model.mapper.toFoundServiceInfo
import dev.nordix.service_manager.domain.model.mapper.toLocalServiceInfo
import dev.nordix.service_manager.domain.model.mapper.toServiceInfo
import dev.nordix.settings.TerminalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class NsdServicesStateProvider(
    val holder: MutableStateFlow<ServicesStateHolder> = MutableStateFlow(ServicesStateHolder()),
    private val terminalRepository: TerminalRepository,
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
        if (serviceInfo?.terminalId != terminalRepository.terminal.id.value.toString()) {
            Log.e(tag, "serviceName = ${serviceInfo?.serviceName}, terminalId = ${terminalRepository.terminal.id.value}")
        serviceInfo?.let { update { state ->
                state.copy(
                    foundServiceStates = state.foundServiceStates.toMutableList().apply {
                        add(
                            FoundServiceState(
                                status = ServiceStatus.Found,
                                serviceInfo = serviceInfo.toFoundServiceInfo()
                            )
                        )
                    }
                )
            } }
        } else {
            Log.i(tag, "it is my own service")
        }
    }

    fun onServiceLost(serviceInfo: NsdServiceInfo?) {
        Log.e(tag, "onServiceLost: serviceInfo = $serviceInfo")

        if (serviceInfo?.terminalId != terminalRepository.terminal.id.value.toString()) {
            serviceInfo?.let {
                update { state ->
                    state.copy(
                        foundServiceStates = state.foundServiceStates.toMutableList().apply {
                            removeAll { it.serviceInfo.name == serviceInfo.serviceName }
                        },
                        resolvedServiceStates = state.resolvedServiceStates.toMutableList().apply {
                            removeAll { it.serviceInfo.name == serviceInfo.serviceName }
                        },
                    )
                }
            }
        } else {
            Log.i(tag, "it is my own service")
        }
    }

    fun onRegistrationFailed(
        serviceInfo: NsdServiceInfo?,
        errorCode: Int
    ) {
        Log.e(tag, "onRegistrationFailed: serviceInfo = $serviceInfo, errorCode = $errorCode")
        serviceInfo?.let {
            update { state ->
                val serviceIndex = state.localServiceStates
                    .indexOfFirst { it.serviceInfo.name == serviceInfo.serviceName }
                if (serviceIndex != -1) {
                    state.copy(
                        localServiceStates = state.localServiceStates.toMutableList().apply {
                            this[serviceIndex].copy(
                                status = ServiceStatus.RegistrationFailed,
                                serviceInfo = serviceInfo.toLocalServiceInfo()
                            )
                        }
                    )
                } else {
                    state.copy(
                        localServiceStates = state.localServiceStates.toMutableList().apply {
                            add(
                                LocalServiceState(
                                    status = ServiceStatus.RegistrationFailed,
                                    serviceInfo = serviceInfo.toLocalServiceInfo()
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    fun onUnregistrationFailed(
        serviceInfo: NsdServiceInfo?,
        errorCode: Int
    ) {
        Log.e(tag, "onUnregistrationFailed: serviceInfo = $serviceInfo, errorCode = $errorCode")
        serviceInfo?.let {
            update { state ->
                val serviceIndex = state.localServiceStates
                    .indexOfFirst { it.serviceInfo.name == serviceInfo.serviceName }
                if (serviceIndex != -1) {
                    state.copy(
                        localServiceStates = state.localServiceStates.toMutableList().apply {
                            this[serviceIndex].copy(
                                status = ServiceStatus.UnregistrationFailed,
                                serviceInfo = serviceInfo.toLocalServiceInfo()
                            )
                        }
                    )
                } else {
                    state.copy(
                        localServiceStates = state.localServiceStates.toMutableList().apply {
                            add(
                                LocalServiceState(
                                    status = ServiceStatus.UnregistrationFailed,
                                    serviceInfo = serviceInfo.toLocalServiceInfo()
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    fun onServiceRegistered(serviceInfo: NsdServiceInfo?) {
        Log.e(tag, "onServiceRegistered: serviceInfo = $serviceInfo")
        serviceInfo?.let {
            update { state ->
                val serviceIndex = state.localServiceStates
                    .indexOfFirst { it.serviceInfo.name == serviceInfo.serviceName }
                if (serviceIndex != -1) {
                    state.copy(
                        localServiceStates = state.localServiceStates.toMutableList().apply {
                            this[serviceIndex].copy(
                                status = ServiceStatus.Registered,
                                serviceInfo = serviceInfo.toLocalServiceInfo()
                            )
                        }
                    )
                } else {
                    state.copy(
                        localServiceStates = state.localServiceStates.toMutableList().apply {
                            add(
                                LocalServiceState(
                                    status = ServiceStatus.Registered,
                                    serviceInfo = serviceInfo.toLocalServiceInfo()
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) {
        Log.e(tag, "onServiceUnregistered: serviceInfo = $serviceInfo")
        serviceInfo?.let {
            update { state ->
                val serviceIndex = state.localServiceStates
                    .indexOfFirst { it.serviceInfo.name == serviceInfo.serviceName }
                if (serviceIndex != -1) {
                    state.copy(
                        localServiceStates = state.localServiceStates.toMutableList().apply {
                            this[serviceIndex].copy(
                                status = ServiceStatus.Unregistered,
                                serviceInfo = serviceInfo.toLocalServiceInfo()
                            )
                        }
                    )
                } else {
                    state.copy(
                        localServiceStates = state.localServiceStates.toMutableList().apply {
                            add(
                                LocalServiceState(
                                    status = ServiceStatus.Unregistered,
                                    serviceInfo = serviceInfo.toLocalServiceInfo()
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    fun onResolveFailed(
        serviceInfo: NsdServiceInfo?,
        errorCode: Int
    ) {
        Log.e(tag, "onResolveFailed: serviceInfo = $serviceInfo, errorCode = $errorCode")
        update { state ->
            val serviceIndex = state.resolvedServiceStates
                .indexOfFirst { it.serviceInfo.name == serviceInfo?.serviceName }
            if (serviceIndex > -1) {
                val newResolveState = state.resolvedServiceStates[serviceIndex]
                    .copy(status = ServiceStatus.ResolveFailed)
                state.copy(
                    resolvedServiceStates = state.resolvedServiceStates.toMutableList().apply {
                        this[serviceIndex] = newResolveState }
                )
            } else state
        }
    }

    fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
        Log.e(tag, "onServiceResolved: serviceInfo = $serviceInfo")
        update { state ->
            val serviceIndex = state.resolvedServiceStates
                .indexOfFirst { it.serviceInfo.name == serviceInfo?.serviceName }
            if (serviceIndex > -1) {
                val currentResolveState = state.resolvedServiceStates[serviceIndex]
                val newResolveState = if (currentResolveState.status != ServiceStatus.Connected) {
                    currentResolveState.copy(status = ServiceStatus.Resolved)
                } else {
                    currentResolveState

                }
                state.copy(
                    resolvedServiceStates = state.resolvedServiceStates.toMutableList().apply {
                        this[serviceIndex] = newResolveState }
                )
            } else {
                serviceInfo?.let {
                    state.copy(
                        resolvedServiceStates = state.resolvedServiceStates.toMutableList().apply {
                            add(
                                ResolvedServiceState(
                                    status = ServiceStatus.Resolved,
                                    serviceInfo = serviceInfo.toServiceInfo()
                                )
                            )
                        }
                    )
                } ?: state
            }
        }
    }

}