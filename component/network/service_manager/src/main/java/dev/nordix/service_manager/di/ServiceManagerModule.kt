package dev.nordix.service_manager.di

import android.content.Context
import android.net.nsd.NsdManager
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.nordix.service_manager.domain.model.ServicesStateHolder
import dev.nordix.service_manager.holder.NsdServicesStateProvider
import dev.nordix.settings.TerminalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceManagerModule {

    @Provides
    @Singleton
    fun provideServicesStateProvider(
        terminalRepository: TerminalRepository,
    ) : NsdServicesStateProvider = NsdServicesStateProvider(
        holder = MutableStateFlow(ServicesStateHolder()),
        terminalRepository = terminalRepository,
    )

    @Provides
    @Singleton
    fun provideNsdManager(
        @ApplicationContext context: Context,
    ) : NsdManager = ContextCompat.getSystemService(context, NsdManager::class.java) as NsdManager

}
