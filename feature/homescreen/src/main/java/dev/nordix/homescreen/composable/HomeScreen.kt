package dev.nordix.homescreen.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.nordix.homescreen.HomeScreenViewModel
import dev.nordix.homescreen.navigation.NordixTab
import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceInfo

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
) {
    val tabs = remember { NordixTab.tabs }
    var selectedTabIndex by remember { mutableStateOf(tabs.indexOf(NordixTab.ResolvedServices)) }

    val servicesState by viewModel.serviceStates.collectAsState()
    val selectedServices by viewModel.selectedServices.collectAsState()

    val selectedList = remember(selectedTabIndex, servicesState) {
        when (tabs[selectedTabIndex]) {
            NordixTab.FoundServices -> servicesState.foundServiceStates
            NordixTab.ResolvedServices -> servicesState.resolvedServiceStates
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            NordixTopBar(
                onNavigateBack = { viewModel.closeServices() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = { selectedTabIndex = index }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(imageVector = tab.icon, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(text = stringResource(tab.name))
                        }
                    }
                }
            }

            if (selectedServices != null) {
                ServicesScreen(
                    services = selectedServices!!,
                    onAction = { action ->
                        viewModel.acceptAction(action)
                    }
                )
            } else {
                ServicesList(
                    services = selectedList,
                    onGoToServices = { service ->
                        (service as? ResolvedServiceInfo)?.let(viewModel::openServices)
                    }
                )
            }

        }
    }
}
