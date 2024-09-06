package dev.nordix.resourcesharing.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.nordix.core.annotations.OnApplicationCreated
import dev.nordix.discovery.domain.DiscoveryService
import dev.nordix.server_provider.domain.WssServerProvider

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @IntoSet
    fun provideOnApplicationCreated(
        discoveryService: DiscoveryService,
        wssServerProvider: WssServerProvider
    ): OnApplicationCreated = OnApplicationCreated {
        discoveryService.startLookup()
        wssServerProvider.getServer().start()
    }

}
