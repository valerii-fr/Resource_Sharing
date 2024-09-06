package dev.nordix.server_provider.domain

interface WssServerProvider {
    fun getServer() : io.ktor.server.netty.NettyApplicationEngine
}
