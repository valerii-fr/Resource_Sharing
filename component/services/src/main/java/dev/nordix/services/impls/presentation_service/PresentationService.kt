package dev.nordix.services.impls.presentation_service

import dev.nordix.core.Constants.ROOT_SERVICE_PORT
import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.model.ServiceContract
import dev.nordix.services.domain.model.ServiceDescriptor
import dev.nordix.services.domain.model.actions.ServicesPresentationAction
import dev.nordix.services.domain.model.actions.ServicesPresentationAction.ClientPresentation
import dev.nordix.services.domain.model.actions.ServicesPresentationAction.ServerPresentation
import dev.nordix.services.domain.model.actions.ServicesPresentationResult
import dev.nordix.settings.TerminalRepository
import javax.inject.Inject

class PresentationService @Inject constructor(
    terminalRepository: TerminalRepository,
) : NordixTcpService<ServicesPresentationAction<ServicesPresentationResult>, ServicesPresentationResult> {

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
            is ClientPresentation -> ServicesPresentationResult.ClientPresentationResult.Success
        }
    }

}