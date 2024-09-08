package dev.nordix.service_manager.domain.model.mapper

import android.net.nsd.NsdServiceInfo
import dev.nordix.core.Constants
import dev.nordix.service_manager.domain.model.FoundServiceInfo
import dev.nordix.service_manager.domain.model.found.LocalServiceInfo
import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceInfo

fun NsdServiceInfo.toFoundServiceInfo() : FoundServiceInfo =
    FoundServiceInfo(
        name = serviceName,
        type = serviceType,
    )


fun NsdServiceInfo.toServiceInfo() : ResolvedServiceInfo =
    ResolvedServiceInfo(
        name = serviceName,
        type = serviceType ?: (Constants.ROOT_SERVICE_TYPE + "nulled"),
        deviceId = serviceName.substringBefore("/"),
        port = port,
        address = host
    )

fun NsdServiceInfo.toLocalServiceInfo() : LocalServiceInfo =
    LocalServiceInfo(
        name = serviceName,
        type = serviceType ?: (Constants.ROOT_SERVICE_TYPE + "nulled"),
        port = port,
    )

fun ResolvedServiceInfo.toNsdService() : NsdServiceInfo = NsdServiceInfo().apply {
    port = this@toNsdService.port
    serviceName = "$deviceId/$name"
    serviceType = type
}

val NsdServiceInfo.terminalId get() = this.serviceName.substringBefore("/")