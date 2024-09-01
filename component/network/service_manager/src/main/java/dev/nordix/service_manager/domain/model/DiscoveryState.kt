package dev.nordix.service_manager.domain.model

data class DiscoveryState(
    val type: String,
    val state: State
) {

    enum class State {
        Active,
        Error,
        Stopped,
    }

}
