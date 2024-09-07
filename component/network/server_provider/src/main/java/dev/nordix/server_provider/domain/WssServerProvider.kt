package dev.nordix.server_provider.domain

import io.ktor.server.netty.NettyApplicationEngine

interface WssServerProvider {
    fun getServer() : NettyApplicationEngine
}
