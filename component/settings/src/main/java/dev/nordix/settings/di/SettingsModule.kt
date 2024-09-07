package dev.nordix.settings.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.nordix.settings.TerminalRepository
import dev.nordix.settings.domain.TerminalRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {

    @Provides
    @Singleton
    fun provideTerminalRepository(
        @ApplicationContext context: Context,
    ) : TerminalRepository = TerminalRepositoryImpl(
        context = context
    )

}