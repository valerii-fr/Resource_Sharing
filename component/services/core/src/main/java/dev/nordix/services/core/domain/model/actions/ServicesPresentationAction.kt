package dev.nordix.services.domain.model.actions

import dev.nordix.core.utils.InstantSerializer
import dev.nordix.services.domain.model.actions.ServicesPresentationResult.ClientPresentationResult
import dev.nordix.services.domain.model.actions.ServicesPresentationResult.ServerPresentationResult
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
sealed interface ServicesPresentationAction <R: ServicesPresentationResult> : ServiceAction<R> {

    val presentationType: PresentationType

    enum class PresentationType {
        PRESENTATION_INITIAL,
        PRESENTATION_UPDATE,
    }

    @Serializable
    data class ServerPresentation(
        val terminalId: String,
        @Serializable(with = InstantSerializer::class)
        val timestamp: Instant,
        val serviceAliases: List<String>,
        val knownDevices: List<String> = emptyList(),
        override val presentationType: PresentationType = PresentationType.PRESENTATION_INITIAL,
    ) : ServicesPresentationAction<ServerPresentationResult>

    @Serializable
    data class ClientPresentation(
        val terminalId: String,
        val name: String,
        val serviceAliases: List<String>,
        val knownDevices: List<String> = emptyList(),
        override val presentationType: PresentationType = PresentationType.PRESENTATION_INITIAL,
    ) : ServicesPresentationAction<ClientPresentationResult>

}
