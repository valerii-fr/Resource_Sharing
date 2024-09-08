package dev.nordix.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.nordix.presentation.impl.PresentationServiceImpl
import dev.nordix.services.NordixTcpService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationServiceModule {

    @Provides
    @Singleton
    @IntoSet
    fun providePresentationService(
        presentationService: PresentationServiceImpl
    ): NordixTcpService<*, *> = presentationService

}
