package dev.nordix.services.domain.model.actions

import dev.nordix.services.domain.model.results.ServiceActionResult
import kotlinx.serialization.Serializable

@Serializable
sealed interface ServiceAction <out R: ServiceActionResult>
