package dev.nordix.services.utils

import dev.nordix.services.NordixTcpService
import dev.nordix.services.domain.model.actions.ServiceAction
import dev.nordix.services.domain.model.actions.ServiceActionResult

internal inline fun <reified S : NordixTcpService<*, *>> S.typify(
): NordixTcpService<ServiceAction<ServiceActionResult>, ServiceActionResult> {
    return try {
        @Suppress("UNCHECKED_CAST")
        this as NordixTcpService<ServiceAction<ServiceActionResult>, ServiceActionResult>
    } catch (_: Throwable) {
        throw UnsupportedOperationException("Cannot proceed with $this")
    }
}


