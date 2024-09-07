package dev.nordix.settings.domain

import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.nordix.core.model.Terminal
import dev.nordix.settings.TerminalRepository
import javax.inject.Inject

class TerminalRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : TerminalRepository {

    override val terminal = Terminal(
        id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID).let(Terminal::ID),
        name = "default terminal"
    )

}
