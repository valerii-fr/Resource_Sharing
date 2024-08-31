package dev.nordix.presentation.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.nordix.core.annotations.OnApplicationCreated
import dev.nordix.presentation.PresentationService
import dev.nordix.presentation.domain.PresentationServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PresentationModule {

    @Provides
    @Singleton
    fun providePresentationService(
        @ApplicationContext context: Context,
    ) : PresentationService = PresentationServiceImpl(

    )

    @IntoSet
    @Provides
    fun provideOnAppCreated(
        presentationService: PresentationService,
    ) : OnApplicationCreated =
        OnApplicationCreated { context ->
            presentationService.populateServices()
        }

}
