package dev.nordix.services.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.nordix.services.NordixTcpService
import dev.nordix.services.ServiceRepository
import dev.nordix.services.impls.flash_service.FlashService
import dev.nordix.services.impls.message_service.SmsService
import dev.nordix.services.repository.ServiceRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Provides
    @Singleton
    fun provideServiceRepository(
        services: Set<@JvmSuppressWildcards NordixTcpService<*,*>>
    ) : ServiceRepository = ServiceRepositoryImpl(
        services = services
    )

    @Provides
    @Singleton
    @IntoSet
    fun provideSmsService(smsService: SmsService): NordixTcpService<*, *> = smsService

    @Provides
    @Singleton
    @IntoSet
    fun provideFlashService(flashService: FlashService): NordixTcpService<*, *> = flashService

}
