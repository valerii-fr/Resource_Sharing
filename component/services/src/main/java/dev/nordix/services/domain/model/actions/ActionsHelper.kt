@file:Suppress("UNCHECKED_CAST")

package dev.nordix.services.domain.model.actions

import android.util.Log
import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.model.ActionsWrapper
import kotlin.collections.flatMap
import kotlin.reflect.KClass


fun List<String>.mapActionsFromAliases(): List<ActionsWrapper> {
    val aliasesToFilter = this.toSet()
    Log.d("HomeScreenViewModel", "mapActionsFromAliases: filter $aliasesToFilter")
    return NordixTcpService::class.sealedSubclasses.mapNotNull { subclass ->
        Log.d("HomeScreenViewModel", "mapActionsFromAliases: subclass = ${subclass.qualifiedName}")
        if (subclass.qualifiedName !in aliasesToFilter) return@mapNotNull null

        val actionTopLevel = subclass.getAction()
        Log.d("HomeScreenViewModel", "mapActionsFromAliases: actionTopLevel = $actionTopLevel")
        val actions = listOf(actionTopLevel).retrieveSubclasses()
        Log.d("HomeScreenViewModel", "mapActionsFromAliases: actions = $actions")
        val atl = actionTopLevel as? KClass<ServiceAction<*>>
        Log.d("HomeScreenViewModel", "mapActionsFromAliases: atl = $atl")
        val acs = actions as? List<KClass<ServiceAction<*>>>
        Log.d("HomeScreenViewModel", "mapActionsFromAliases: acs = $acs")
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