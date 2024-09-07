package dev.nordix.client_provider.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.nordix.client_provider.data.WssClientProviderImpl
import dev.nordix.client_provider.domain.WssClientProvider
import dev.nordix.services.ServiceRepository
import dev.nordix.settings.TerminalRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WssClientProviderModule {

    @Provides
    fun provideHttpClient() : HttpClient = HttpClient(CIO) {
        install(WebSockets)
    }

    @Provides
    @Singleton
    fun provideWssClient(
        httpClient: HttpClient,
        serviceRepository: ServiceRepository,
        terminalRepository: TerminalRepository,
    ) : WssClientProvider = WssClientProviderImpl(
        httpClient = httpClient,
        serviceRepository = serviceRepository,
        terminalRepository = terminalRepository,
    )

}
