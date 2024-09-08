package dev.nordix.services.domain.model

import dev.nordix.services.domain.model.actions.ServiceAction
import kotlin.reflect.KClass

data class ActionsWrapper(
    val rootAction: KClass<ServiceAction<*>>,
    val actions: List<KClass<ServiceAction<*>>>,
)
