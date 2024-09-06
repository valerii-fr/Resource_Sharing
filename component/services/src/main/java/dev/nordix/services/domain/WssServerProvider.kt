package dev.nordix.services.domain

import io.ktor.server.netty.NettyApplicationEngine

interface WssServerProvider {
    fun getServer() : NettyApplicationEngine
}
