package dev.nordix.homescreen.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.nordix.homescreen.HomeScreenViewModel
import dev.nordix.homescreen.navigation.NavigationDirections
import dev.nordix.homescreen.navigation.NordixTab
import dev.nordix.homescreen.navigation.argumentList
import dev.nordix.homescreen.navigation.route

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
) {

    val navController = rememberNavController()
    val servicesState by viewModel.serviceStates.collectAsState()
    val tabs = remember { NordixTab.tabs }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier,
        topBar = {
            NordixTopBar(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column {

            TabRow(
                selectedTabIndex = selectedTabIndex,
            ) {
                tabs.forEach { tab ->
                    Tab(
                        selected = tab == tabs[selectedTabIndex],
                        onClick = {
                            selectedTabIndex == tabs.indexOf(tab)
                            navController.navigate(tab.route)
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = tab.icon, contentDescription = null)
                            Text(text = stringResource(tab.name))
                        }
                    }
                }
            }

            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = NavigationDirections.MyServices.route
            ) {

                composable(
                    route = NavigationDirections.MyServices.route,
                    arguments = NavigationDirections.MyServices.argumentList
                ) {
                    ServicesList(services = servicesState.localServiceStates)
                }

                composable(
                    route = NavigationDirections.FoundServices.route,
                    arguments = NavigationDirections.FoundServices.argumentList
                ) {
                    ServicesList(services = servicesState.foundServiceStates)
                }

                composable(
                    route = NavigationDirections.ResolvedServices.route,
                    arguments = NavigationDirections.ResolvedServices.argumentList
                ) {
                    ServicesList(services = servicesState.resolvedServiceStates)
                }

            }
        }
    }
}