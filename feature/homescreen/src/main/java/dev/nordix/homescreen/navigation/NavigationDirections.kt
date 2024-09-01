package dev.nordix.homescreen.navigation

import com.compose.type_safe_args.annotation.ComposeDestination

sealed interface NavigationDirections {

    @ComposeDestination
    interface MyServices {
        companion object
    }

    @ComposeDestination
    interface FoundServices {
        companion object
    }

    @ComposeDestination
    interface ResolvedServices {
        companion object
    }

}
