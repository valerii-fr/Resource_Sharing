package dev.nordix.client_provider.domain

import dev.nordix.client_provider.domain.model.ClientTarget
import dev.nordix.services.domain.model.actions.ServiceInteraction
import kotlinx.coroutines.flow.StateFlow

interface WssClientProvider {

    val activeClients: StateFlow<List<ClientTarget>>

    suspend fun postInteraction(target: ClientTarget, action: ServiceInteraction)

    suspend fun broadcastInteraction(action: ServiceInteraction)

    suspend fun launchClient(target: ClientTarget)

    suspend fun terminateClient(host: String)

}
