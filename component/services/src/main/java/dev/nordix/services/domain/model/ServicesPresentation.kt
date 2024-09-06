package dev.nordix.services.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ServicesPresentation(
    @Contextual val timestamp: Instant,
    val serviceAliases: List<String>,
)
