package dev.nordix.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nordix.client_provider.domain.WssClientProvider
import dev.nordix.client_provider.domain.model.ClientTarget
import dev.nordix.homescreen.model.ServiceActionsWrapper
import dev.nordix.service_manager.domain.model.ServiceState
import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceInfo
import dev.nordix.service_manager.holder.NsdServicesStateProvider
import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.actions.mapActionsFromAliases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val nsdServicesStateProvider: NsdServicesStateProvider,
    private val wssClientProvider: WssClientProvider,
) : ViewModel() {

    val serviceStates = nsdServicesStateProvider.holder.asStateFlow()
    val selectedService : StateFlow<ServiceActionsWrapper?>
        field = MutableStateFlow(null)

    fun acceptAction(action: ServiceAction<*>) {
        Log.i("HomeScreenViewModel", "acceptAction: $action")
        viewModelScope.launch {
            serviceStates.value.resolvedServiceStates
                .filter { it.status == ServiceState.ServiceStatus.Connected }
                .find {
                    it.serviceInfo.deviceId == selectedService.value?.serviceInfo?.deviceId
                }
                ?.let { service ->
                    wssClientProvider.postInteraction(
                        target = ClientTarget(
                            host = service.serviceInfo.address?.hostAddress ?: "127.0.0.1",
                            serviceName = service.serviceInfo.name
                        ),
                        action = action
                    )
                }
        }
    }

    fun openServices(serviceInfo: ResolvedServiceInfo) {
        Log.i("HomeScreenViewModel", "openServices: $serviceInfo")
        Log.i("HomeScreenViewModel", "resolvedServices: ${serviceStates.value.resolvedServiceStates}")
        selectedService.update { services ->
            serviceStates.value.resolvedServiceStates.find {
                it.serviceInfo.deviceId == serviceInfo.deviceId
            }?.let { service ->
                val saw = ServiceActionsWrapper(
                    serviceInfo = service.serviceInfo,
                    service.serviceInfo.serviceAliases
                        .filter { it != NordixTcpService.PresentationService::class.qualifiedName }
                        .mapActionsFromAliases()
                )
                Log.d("HomeScreenViewModel", "openServices: $saw")
                saw
            } ?: services
        }
    }

    fun closeServices() {
        selectedService.update { null }
    }

}