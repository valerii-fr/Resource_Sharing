package dev.nordix.services.domain.model.actions

import dev.nordix.core.utils.InstantSerializer
import dev.nordix.services.domain.model.actions.ServicesPresentationResult.ClientPresentationResult
import dev.nordix.services.domain.model.actions.ServicesPresentationResult.ServerPresentationResult
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
sealed interface ServicesPresentationAction <R: ServicesPresentationResult> : ServiceAction<R> {

    @Serializable
    data class ServerPresentation(
        val terminalId: String,
        @Serializable(with = InstantSerializer::class)
        val timestamp: Instant,
        val serviceAliases: List<String>,
    ) : ServicesPresentationAction<ServerPresentationResult>

    @Serializable
    data class ClientPresentation(
        val terminalId: String,
        val name: String,
        val serviceAliases: List<String>,
    ) : ServicesPresentationAction<ClientPresentationResult>

}
