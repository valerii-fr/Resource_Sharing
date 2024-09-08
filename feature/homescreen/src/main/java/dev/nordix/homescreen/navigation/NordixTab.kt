package dev.nordix.homescreen.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import dev.nordix.common_ui.R

sealed interface NordixTab {

    val icon: ImageVector
    val route: String
    @get: StringRes val name: Int

    object FoundServices : NordixTab {
        override val icon: ImageVector = Icons.Default.Search
        override val name: Int = R.string.found_services
        override val route: String = NavigationDirections.FoundServices.route
    }

    object ResolvedServices : NordixTab {
        override val icon: ImageVector = Icons.Default.Link
        override val name: Int = R.string.resolved_services
        override val route: String = NavigationDirections.ResolvedServices.route
    }

    companion object {
        val tabs = listOf(
            FoundServices,
            ResolvedServices,
        )
    }

}