package dev.nordix.services.domain.model

import dev.nordix.core.utils.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ServicesPresentation(
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant,
    val serviceAliases: List<String>,
)
