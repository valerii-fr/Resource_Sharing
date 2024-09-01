package dev.nordix.service_manager.domain.model.mapper

import android.net.nsd.NsdServiceInfo
import dev.nordix.common.model.FoundServiceInfo
import dev.nordix.core.Constants
import dev.nordix.service_manager.domain.model.ServiceInfo

fun NsdServiceInfo.toFoundServiceInfo() : FoundServiceInfo =
    FoundServiceInfo(
        name = serviceName,
        type = serviceType,
        deviceId = serviceName.substringBefore("/")
    )


fun NsdServiceInfo.toServiceInfo() : ServiceInfo =
    ServiceInfo(
        name = serviceName ?: (Constants.ROOT_SERVICE_NAME + "nulled"),
        type = serviceType ?: (Constants.ROOT_SERVICE_TYPE + "nulled"),
        deviceId = serviceName.substringBefore("/"),
        port = port,
        address = host
    )

val NsdServiceInfo.terminalId get() = this.serviceName.substringBefore("/")