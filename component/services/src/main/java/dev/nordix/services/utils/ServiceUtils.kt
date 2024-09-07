package dev.nordix.services.utils

import android.util.Log
import dev.nordix.core.utils.getGenericTypesOf
import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.actions.ServiceActionResult
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes


suspend fun Set<@JvmSuppressWildcards NordixTcpService<*, *>>.act(
    action: ServiceAction<ServiceActionResult>
) : ServiceActionResult? {
    val requiredTypes = action::class.allSupertypes.map { it.classifier }.filter { it is KClass<*> }
    return firstOrNull { service ->
        service.getGenericTypesOf(NordixTcpService::class).first() in requiredTypes
    }
        ?.let { service ->
            service.typify().act(action)
        } ?: run {
            Log.e(this::class.simpleName, "service not found for action $action")
            null
        }
}
