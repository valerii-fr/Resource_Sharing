package dev.nordix.resourcesharing.di

import androidx.test.espresso.core.internal.deps.dagger.Provides
import androidx.test.espresso.core.internal.deps.dagger.multibindings.IntoSet
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.nordix.core.annotations.OnApplicationCreated
import dev.nordix.discovery.domain.DiscoveryService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    @IntoSet
    fun provideOnAppCreated(
        discoveryService: DiscoveryService
    ): OnApplicationCreated  = OnApplicationCreated {
        discoveryService.startRootServiceLookup()
    }

}
