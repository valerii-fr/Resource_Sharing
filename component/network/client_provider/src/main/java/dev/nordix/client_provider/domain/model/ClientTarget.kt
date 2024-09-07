package dev.nordix.client_provider.domain.model

import dev.nordix.core.Constants.ROOT_SERVICE_PORT
import dev.nordix.core.Constants.ROOT_WS_PATH

data class ClientTarget(
    val host: String,
    val port: Int = ROOT_SERVICE_PORT,
    val path: String = ROOT_WS_PATH,
)
