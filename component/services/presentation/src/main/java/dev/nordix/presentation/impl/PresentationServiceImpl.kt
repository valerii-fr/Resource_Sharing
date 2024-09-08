package dev.nordix.presentation.impl

import dev.nordix.core.Constants.ROOT_SERVICE_PORT
import dev.nordix.service_manager.holder.NsdServicesStateProvider
import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.model.ServiceContract
import dev.nordix.services.domain.model.ServiceDescriptor
import dev.nordix.services.domain.model.actions.ServicesPresentationAction
import dev.nordix.services.domain.model.actions.ServicesPresentationAction.ClientPresentation
import dev.nordix.services.domain.model.actions.ServicesPresentationAction.ServerPresentation
import dev.nordix.services.domain.model.actions.ServicesPresentationResult
import dev.nordix.settings.TerminalRepository
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.apply
import kotlin.collections.indexOfFirst
import kotlin.collections.toMutableList

class PresentationServiceImpl @Inject constructor(
    terminalRepository: TerminalRepository,
    private val serviceStateProvider: NsdServicesStateProvider,
) : NordixTcpService.PresentationService() {

    private val contract = ServiceContract(
        type = ServiceContract.ServiceType.INFO,
        actions = listOfNotNull(
            ServerPresentation::class,
        )
    )

    override val descriptor: ServiceDescriptor = ServiceDescriptor(
        name = "PresentationService",
        owner = terminalRepository.terminal.id,
        contract = contract,
        port = ROOT_SERVICE_PORT,
    )


    override fun terminate() { }

    override suspend fun act(
        action: ServicesPresentationAction<ServicesPresentationResult>
    ): ServicesPresentationResult {
        return when (action) {
            is ServerPresentation -> ServicesPresentationResult.ServerPresentationResult.Success
            is ClientPresentation -> {
                when(action.presentationType) {
                    ServicesPresentationAction.PresentationType.PRESENTATION_UPDATE -> {
                        serviceStateProvider.update { state ->
                            state.copy(
                                resolvedServiceStates = state
                                    .resolvedServiceStates.toMutableList().apply {
                                        val tI = indexOfFirst { service ->
                                            service.terminalId == action.terminalId
                                        }
                                        if (tI == -1) return@apply

                                        val targetItem = this[tI]
                                        this[tI] = targetItem.copy(
                                            serviceInfo = targetItem.serviceInfo.copy(
                                                knownDevices = action.knownDevices,
                                            )
                                        )
                                    }
                            )

                        }
                        ServicesPresentationResult.ClientPresentationResult.Success
                    }
                    ServicesPresentationAction.PresentationType.PRESENTATION_INITIAL -> {
                        ServicesPresentationResult.ClientPresentationResult.Success
                    }
                }
            }
        }
    }

}