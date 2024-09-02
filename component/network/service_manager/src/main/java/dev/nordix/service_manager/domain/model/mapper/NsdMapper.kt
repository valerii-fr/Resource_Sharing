package dev.nordix.service_manager.domain.model.mapper

import android.net.nsd.NsdServiceInfo
import dev.nordix.core.Constants
import dev.nordix.service_manager.domain.model.FoundServiceInfo
import dev.nordix.service_manager.domain.model.LocalServiceInfo
import dev.nordix.service_manager.domain.model.ServiceInfo

fun NsdServiceInfo.toFoundServiceInfo() : FoundServiceInfo =
    FoundServiceInfo(
        name = serviceName.substringAfter("/"),
        type = serviceType,
        deviceId = serviceName.substringBefore("/")
    )


fun NsdServiceInfo.toServiceInfo() : ServiceInfo =
    ServiceInfo(
        name = serviceName?.substringAfter("/") ?: (Constants.ROOT_SERVICE_NAME + "nulled"),
        type = serviceType ?: (Constants.ROOT_SERVICE_TYPE + "nulled"),
        deviceId = serviceName.substringBefore("/"),
        port = port,
        address = host
    )

fun NsdServiceInfo.toLocalServiceInfo() : LocalServiceInfo =
    LocalServiceInfo(
        name = serviceName?.substringAfter("/") ?: (Constants.ROOT_SERVICE_NAME + "nulled"),
        type = serviceType ?: (Constants.ROOT_SERVICE_TYPE + "nulled"),
        port = port,
    )

fun ServiceInfo.toNsdService() : NsdServiceInfo = NsdServiceInfo().apply {
    port = this@toNsdService.port
    serviceName = "$deviceId/$name"
    serviceType = type
}

val NsdServiceInfo.terminalId get() = this.serviceName.substringBefore("/")