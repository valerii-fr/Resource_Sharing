package dev.nordix.client_provider.domain

import dev.nordix.client_provider.domain.model.ClientTarget
import kotlinx.coroutines.flow.StateFlow

interface WssClientProvider {

    val activeClients: StateFlow<List<ClientTarget>>

    suspend fun launchClient(target: ClientTarget)

    suspend fun terminateClient(host: String)

}
