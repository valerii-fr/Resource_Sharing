@file:Suppress("UNCHECKED_CAST")

package dev.nordix.services.domain.model.actions

import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.model.ActionsWrapper
import kotlin.collections.flatMap
import kotlin.reflect.KClass


fun List<String>.mapActionsFromAliases(): List<ActionsWrapper> {
    val aliasesToFilter = this.toSet()
    return NordixTcpService::class.sealedSubclasses.mapNotNull { subclass ->
        if (subclass.simpleName !in aliasesToFilter) return@mapNotNull null

        val actionTopLevel = subclass.getAction()
        val actions = listOf(actionTopLevel).retrieveSubclasses()

        val atl = actionTopLevel as? KClass<ServiceAction<*>>
        val acs = actions as? List<KClass<ServiceAction<*>>>
        if (atl != null && acs != null) {
            ActionsWrapper(
                rootAction = atl,
                actions = acs,
            )
        } else null
    }
}

private fun KClass<out NordixTcpService<*, *>>.getAction() = supertypes.flatMap { supertype ->
    supertype.arguments.mapNotNull { arg ->
        (arg.type?.classifier as? KClass<*>)
    }
}.first()

private fun List<KClass<*>>.retrieveSubclasses() : List<KClass<*>> {
    return flatMap { action ->
        action.let {
            it.sealedSubclasses.firstOrNull()?.let { kClass ->
                it.sealedSubclasses
            }
        }.orEmpty()
    }.apply {
        if (this.isNotEmpty()) retrieveSubclasses()
    }
}