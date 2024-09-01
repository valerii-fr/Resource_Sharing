package dev.nordix.settings.domain

import dev.nordix.core.model.Terminal
import dev.nordix.settings.TerminalRepository
import javax.inject.Inject

class TerminalRepositoryImpl @Inject constructor() : TerminalRepository {

    override fun getTerminal(): Terminal {
        return Terminal(
            id = Terminal.ID.new()
        )
    }

}
