package dev.nordix.service_manager.domain.model

import android.net.nsd.NsdServiceInfo
import dev.nordix.common.model.FoundServiceInfo

fun NsdServiceInfo.toFoundServiceInfo() : FoundServiceInfo =
    FoundServiceInfo(
        name = serviceName,
        type = serviceType,
        deviceId = serviceName.substringBefore(":")
    )


fun NsdServiceInfo.toServiceInfo() : ServiceInfo =
    ServiceInfo(
        name = serviceName,
        type = serviceType,
        deviceId = serviceName.substringBefore(":"),
        port = port,
        address = host
    )
