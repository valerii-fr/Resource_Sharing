package dev.nordix.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nordix.homescreen.composable.UiServiceAction
import dev.nordix.homescreen.model.ServiceActionsWrapper
import dev.nordix.service_manager.domain.model.ServiceInfo
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
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val nsdServicesStateProvider: NsdServicesStateProvider,
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
    }

    fun openServices(serviceInfo: ServiceInfo) {
        selectedServices.update { services ->
            serviceStates.value.resolvedServiceStates.find {
                it.serviceInfo.name == serviceInfo.name &&
                it.serviceInfo.type == serviceInfo.type &&
                it.serviceInfo.port == serviceInfo.port
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