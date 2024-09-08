package dev.nordix.homescreen.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.nordix.service_manager.domain.model.ServiceInfo
import dev.nordix.service_manager.domain.model.ServiceState

@Composable
fun ServicesList(
    modifier: Modifier = Modifier,
    services: List<ServiceState>,
    onAction: (ServiceInfo, UiServiceAction) -> Unit = { _, _ -> },
    onGoToServices: (ServiceInfo) -> Unit = { },
) {

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState
    ) {
        items(
            items = services,
            key = { item -> item.serviceInfo.toString() + item.serviceInfo.name }
        ) { item ->
            ServiceItem(
                state = item,
                onGoToServices = {
                    onGoToServices(item.serviceInfo)
                },
                onAction = { action ->
                    onAction(item.serviceInfo, action)
                }
            )
        }
    }

}
