package dev.nordix.homescreen.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nordix.service_manager.domain.model.ServiceState
import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceState

@Composable
fun ServiceItem(
    modifier: Modifier = Modifier,
    state: ServiceState,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Presentation(state)
        }
    }
}

@Composable
private fun RowScope.Presentation(state: ServiceState) {
    Column {
        Text(text = state.name, style = MaterialTheme.typography.bodySmall)
        Text(text = state.serviceInfo.type, style = MaterialTheme.typography.bodySmall)
        Text(text = state.serviceInfo.port.toString(), style = MaterialTheme.typography.bodySmall)
        if (state is ResolvedServiceState) {
            Text(text = state.serviceInfo.deviceId, style = MaterialTheme.typography.bodySmall)
            Text(text = state.serviceInfo.address.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
    Column(
        horizontalAlignment = Alignment.End
    ) {
        Text(text = state.status.name, style = MaterialTheme.typography.bodySmall)
        if (state is ResolvedServiceState) {
            state.serviceInfo.serviceAliases.forEach {
                Text(text = it.substringAfterLast("."), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
