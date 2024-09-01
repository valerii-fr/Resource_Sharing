package dev.nordix.discovery.di

import android.content.Context
import android.net.nsd.NsdManager
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    ) : ServicesStateProvider = ServicesStateProvider(
        holder = MutableStateFlow(ServicesStateHolder()),
        terminalRepository = terminalRepository,
    )

    @Provides
    @Singleton
    fun provideDiscoveryListener(
        serviceStateProvider: ServicesStateProvider
    ) : DiscoveryListener = DiscoveryListener(serviceStateProvider)

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
