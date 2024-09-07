package dev.nordix.homescreen.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nordix.service_manager.domain.model.ServiceState

@Composable
fun ServicesList(
    modifier: Modifier = Modifier,
    services: List<ServiceState>,
) {

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(8.dp)
    ) {
        itemsIndexed(
            items = services,
            key = { _, item -> item.serviceInfo.toString() + item.serviceInfo.name }
        ) { index, item ->
            ServiceItem(state = item, isLast = index == services.lastIndex)
        }
    }

}