package dev.nordix.discovery.di

import android.net.nsd.NsdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.nordix.discovery.domain.DiscoveryService
import dev.nordix.discovery.service.DiscoveryServiceImpl
import dev.nordix.service_manager.holder.NsdServicesStateProvider
import dev.nordix.client_provider.domain.WssClientProvider
import dev.nordix.settings.TerminalRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DiscoveryModule {

    @Provides
    @Singleton
    fun provideDiscoveryService(
        scope: CoroutineScope,
        nsdManager: NsdManager,
        nsdServicesStateProvider: NsdServicesStateProvider,
        wssClientProvider: WssClientProvider,
        terminalRepository: TerminalRepository,
    ) : DiscoveryService = DiscoveryServiceImpl(
        scope = scope,
        nsdManager = nsdManager,
        nsdServicesStateProvider = nsdServicesStateProvider,
        wssClientProvider = wssClientProvider,
        terminalRepository = terminalRepository,
    )

}
