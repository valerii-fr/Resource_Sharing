package dev.nordix.homescreen.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.nordix.service_manager.domain.model.ServiceState
import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServiceItem(
    modifier: Modifier = Modifier,
    state: ServiceState,
    onGoToServices: () -> Unit,
    onAction: (UiServiceAction) -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxWidth()
            .combinedClickable(
                onClick = onGoToServices,
                onLongClick = { dropdownExpanded = true },
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Presentation(state)

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                UiServiceAction.dropdownActionsShownAlways.forEach {
                    it.Button {
                        onAction(it)
                        dropdownExpanded = false
                    }
                }
                when(state.status) {
                    ServiceState.ServiceStatus.Connected -> UiServiceAction.Disconnect.Button {
                        onAction(it)
                        dropdownExpanded = false
                    }
                    else -> {
                        UiServiceAction.Connect.Button {
                            onAction(it)
                            dropdownExpanded = false
                        }
                    }
                }
            }
        }
        HorizontalDivider(Modifier.padding(horizontal = 4.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RowScope.Presentation(state: ServiceState) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.weight(1f)
    ) {
        Column {
            Text(text = state.status.name, style = MaterialTheme.typography.labelLarge)
            if (state is ResolvedServiceState) {
                state.serviceInfo.serviceAliases.forEach {
                    Text(it.substringAfterLast("."), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            if (state is ResolvedServiceState) {
                Text(state.serviceInfo.deviceId, style = MaterialTheme.typography.labelLarge)
                Text(state.serviceInfo.address.toString().removePrefix("/"), style = MaterialTheme.typography.labelSmall)
            } else {
                Text(state.name, style = MaterialTheme.typography.bodySmall)
                Text(state.serviceInfo.type, style = MaterialTheme.typography.bodySmall)
                Text(state.serviceInfo.port.toString(), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
    Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = null
    )
}

@Composable
private fun UiServiceAction.Button(
    onClick: (UiServiceAction) -> Unit,
) {
    DropdownMenuItem(
        text = { Text(text = stringResource(title)) },
        onClick = { onClick(this) },
        leadingIcon = { Icon(icon, contentDescription = null) }
    )
}
