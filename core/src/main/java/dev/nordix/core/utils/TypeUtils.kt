package dev.nordix.core.utils

import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes

@OptIn(ExperimentalStdlibApi::class)
fun Any.getGenericTypesOf(
    kClass: KClass<*>
): List<KClass<*>> = this::class.allSupertypes
    .find { it.classifier == kClass }
    ?.arguments
    ?.mapNotNull { typeArg ->
        (typeArg.type?.classifier as? KClass<*>)
    }.orEmpty()
