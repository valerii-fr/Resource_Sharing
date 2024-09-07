package dev.nordix.homescreen.model

import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceInfo
import dev.nordix.services.domain.model.ActionsWrapper

data class ServiceActionsWrapper(
    val serviceInfo: ResolvedServiceInfo,
    val actions: List<ActionsWrapper>,
)
