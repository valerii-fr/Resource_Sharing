package dev.nordix.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nordix.client_provider.domain.WssClientProvider
import dev.nordix.client_provider.domain.model.ClientTarget
import dev.nordix.homescreen.composable.UiServiceAction
import dev.nordix.homescreen.model.ServiceActionsWrapper
import dev.nordix.service_manager.domain.model.ServiceInfo
import dev.nordix.service_manager.domain.model.ServiceState
import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceInfo
import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceState
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
    val selectedServices : StateFlow<ServiceActionsWrapper?>
        field = MutableStateFlow(null)

    fun acceptAction(action: UiServiceAction) {
        Log.i("HomeScreenViewModel", "acceptAction: $action")
        when (action) {
            UiServiceAction.Connect -> {}
            UiServiceAction.Disconnect -> {}
            UiServiceAction.Forget -> {}
        }
    }

    fun acceptAction(action: ServiceAction<*>) {
        Log.i("HomeScreenViewModel", "acceptAction: $action")
        viewModelScope.launch {
            serviceStates.value.resolvedServiceStates
                .filter { it.status == ServiceState.ServiceStatus.Connected }
                .find {
                    it.serviceInfo.deviceId == selectedServices.value?.serviceInfo?.deviceId
                }
                ?.let { service ->
                    wssClientProvider.postInteraction(
                        target = ClientTarget(
                            host = service.serviceInfo.address?.hostAddress ?: "127.0.0.1",
                        ),
                        action = action
                    )
                }
        }
    }

    fun openServices(serviceInfo: ResolvedServiceInfo) {
        Log.i("HomeScreenViewModel", "openServices: $serviceInfo")
        Log.i("HomeScreenViewModel", "resolvedServices: ${serviceStates.value.resolvedServiceStates}")
        selectedServices.update { services ->
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
        selectedServices.update { null }
    }

}