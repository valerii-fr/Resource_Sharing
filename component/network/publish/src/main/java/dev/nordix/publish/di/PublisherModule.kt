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
import dev.nordix.publish.listeners.PublishingListener
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
        publishingListener: PublishingListener,
    ) : ServicePublisher = ServicePublisherImpl(
        nsdManager = nsdManager,
        terminalRepository = terminalRepository,
        publishingListener = publishingListener
    )

    @Provides
    @IntoSet
    fun provideOnApplicationCreated(
        servicePublisher: ServicePublisher,
    ) : OnApplicationCreated = OnApplicationCreated {
        servicePublisher.publishRootService()
    }

}
