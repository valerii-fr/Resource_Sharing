package dev.nordix.services.domain.model.actions

import kotlinx.serialization.Serializable

@Serializable
sealed interface ServiceAction <out R: ServiceActionResult> : ServiceInteraction
