package dev.nordix.services.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.nordix.core.annotations.OnApplicationCreated
import dev.nordix.services.NordixTcpService
import dev.nordix.services.ServiceRepository
import dev.nordix.services.domain.WssServerProvider
import dev.nordix.services.domain.server_provider.WssServerProviderImpl
import dev.nordix.services.impls.message_service.SmsService
import dev.nordix.services.repository.ServiceRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Provides
    @Singleton
    fun provideWssServerProvider(
        services: Set<@JvmSuppressWildcards NordixTcpService<*,*>>,
    ): WssServerProvider = WssServerProviderImpl(
        services = services,
    )

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

}
