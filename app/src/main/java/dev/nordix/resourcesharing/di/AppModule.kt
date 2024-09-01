package dev.nordix.resourcesharing.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.nordix.core.annotations.OnApplicationCreated
import dev.nordix.discovery.domain.DiscoveryService

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @IntoSet
    fun provideOnApplicationCreated(
        discoveryService: DiscoveryService
    ): OnApplicationCreated = OnApplicationCreated {
        discoveryService.startRootServiceLookup()
    }

}
