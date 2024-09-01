package dev.nordix.service_manager.domain.model.mapper

import android.net.nsd.NsdServiceInfo
import dev.nordix.common.model.FoundServiceInfo
import dev.nordix.service_manager.domain.model.ServiceInfo

fun NsdServiceInfo.toFoundServiceInfo() : FoundServiceInfo =
    FoundServiceInfo(
        name = serviceName,
        type = serviceType,
        deviceId = serviceName.substringBefore("/")
    )


fun NsdServiceInfo.toServiceInfo() : ServiceInfo =
    ServiceInfo(
        name = serviceName,
        type = serviceType,
        deviceId = serviceName.substringBefore("/"),
        port = port,
        address = host
    )
