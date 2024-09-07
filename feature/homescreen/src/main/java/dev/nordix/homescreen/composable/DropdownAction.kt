package dev.nordix.homescreen.composable

import dev.nordix.common_ui.R
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface UiServiceAction {
    @get:StringRes val title: Int
    val icon: ImageVector

    data object Disconnect : UiServiceAction {
        override val title: Int = R.string.action_disconnect
        override val icon: ImageVector = Icons.Default.LinkOff
    }

    data object Connect : UiServiceAction {
        override val title: Int = R.string.action_connect
        override val icon: ImageVector = Icons.Default.Link
    }

    data object Forget : UiServiceAction {
        override val title: Int = R.string.action_forget
        override val icon: ImageVector = Icons.Default.RemoveCircleOutline
    }

    companion object {
        val dropdownActionsShownAlways = listOf(Forget)
    }

}
