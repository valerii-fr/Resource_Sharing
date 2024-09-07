package dev.nordix.homescreen.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nordix.homescreen.model.ServiceActionsWrapper
import dev.nordix.services.domain.model.ActionsWrapper
import dev.nordix.services.domain.model.actions.ServiceAction
import kotlin.reflect.KClass

@Composable
fun ServicesScreen(
    modifier: Modifier = Modifier,
    services: ServiceActionsWrapper,
    onAction: (ServiceAction<*>) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(
            items = services.actions,
            key = { it.rootAction.simpleName.toString() }
        ) { item ->
            ActionBlock(
                actionsWrapper = item,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun ActionBlock(
    actionsWrapper: ActionsWrapper,
    onAction: (ServiceAction<*>) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(4.dp)
    ) {
        Text(
            text = actionsWrapper.rootAction.simpleName.toString(),
            style = MaterialTheme.typography.titleLarge
        )
        Card(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
            )
        ) {
            actionsWrapper.actions.forEach { action ->
                ActionItem(action = action) {
                    onAction(action.objectInstance as ServiceAction<*>)
                }
            }
        }
    }
}

@Composable
private fun ActionItem(
    action: KClass<ServiceAction<*>>,
    onAct: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(4.dp)
    ) {
        Button(onClick = onAct) {
            Text(text = action.simpleName.toString())
        }
    }
}