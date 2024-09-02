package dev.nordix.services.domain

import dev.nordix.services.NordixTcpService
import io.ktor.server.netty.NettyApplicationEngine

interface WssServerProvider {
    fun getServer(service: NordixTcpService<*,*>) : NettyApplicationEngine
}
