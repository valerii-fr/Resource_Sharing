package dev.nordix.publish.di

import android.net.nsd.NsdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.nordix.core.annotations.OnApplicationCreated
import dev.nordix.publish.ServicePublisher
import dev.nordix.publish.domain.ServicePublisherImpl
import dev.nordix.service_manager.holder.NsdServicesStateProvider
import dev.nordix.settings.TerminalRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PublisherModule {

    @Provides
    @Singleton
    fun provideServicePublisher(
        nsdManager: NsdManager,
        terminalRepository: TerminalRepository,
        nsdServicesStateProvider: NsdServicesStateProvider,
    ) : ServicePublisher = ServicePublisherImpl(
        nsdManager = nsdManager,
        terminalRepository = terminalRepository,
        nsdServicesStateProvider = nsdServicesStateProvider,
    )

    @Provides
    @IntoSet
    fun provideOnApplicationCreated(
        servicePublisher: ServicePublisher,
    ) : OnApplicationCreated = OnApplicationCreated {
        servicePublisher.publishRootService()
    }

}
