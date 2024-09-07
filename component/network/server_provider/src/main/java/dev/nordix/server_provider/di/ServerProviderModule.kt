package dev.nordix.server_provider.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.nordix.server_provider.data.WssServerProviderImpl
import dev.nordix.server_provider.domain.WssServerProvider
import dev.nordix.services.NordixTcpService
import dev.nordix.settings.TerminalRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServerProviderModule {

    @Provides
    @Singleton
    fun provideWssServerProvider(
        services: Set<@JvmSuppressWildcards NordixTcpService<*,*>>,
        terminalRepository: TerminalRepository,
    ): WssServerProvider =
        WssServerProviderImpl(
            services = services,
            terminalRepository = terminalRepository,
        )
}