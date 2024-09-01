package dev.nordix.discovery.di

import android.net.nsd.NsdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.nordix.discovery.domain.DiscoveryService
import dev.nordix.discovery.listeners.DiscoveryListener
import dev.nordix.discovery.service.DiscoveryServiceImpl
import dev.nordix.service_manager.domain.model.ServicesStateHolder
import dev.nordix.service_manager.holder.ServicesStateProvider
import dev.nordix.settings.TerminalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DiscoveryModule {

    @Provides
    @Singleton
    fun provideServicesStateProvider(
        terminalRepository: TerminalRepository,
        nsdManager: NsdManager,
    ) : ServicesStateProvider = ServicesStateProvider(
        holder = MutableStateFlow(ServicesStateHolder()),
        terminalRepository = terminalRepository,
        nsdManager = nsdManager,
    )

    @Provides
    @Singleton
    fun provideDiscoveryListener(
        serviceStateProvider: ServicesStateProvider,
        nsdManager: NsdManager,
    ) : DiscoveryListener = DiscoveryListener(
        servicesStateProvider = serviceStateProvider,
        nsdManager = nsdManager,
    )

    @Provides
    @Singleton
    fun provideDiscoveryService(
        scope: CoroutineScope,
        nsdManager: NsdManager,
        discoveryListener: DiscoveryListener,
    ) : DiscoveryService = DiscoveryServiceImpl(
        scope = scope,
        nsdManager = nsdManager,
        discoveryListener = discoveryListener,
    )

}
