package dev.nordix.homescreen.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nordix.service_manager.domain.model.FoundServiceState
import dev.nordix.service_manager.domain.model.ServiceState

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
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = state.name, style = MaterialTheme.typography.bodySmall)
                Text(text = state.status.name, style = MaterialTheme.typography.bodySmall)
            }
            Text(text = state.serviceInfo.type, style = MaterialTheme.typography.bodySmall)
            Text(text = state.serviceInfo.port.toString(), style = MaterialTheme.typography.bodySmall)
            Text(text = state.serviceInfo.address.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ServiceItem(
    modifier: Modifier = Modifier,
    state: FoundServiceState,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = state.name, style = MaterialTheme.typography.bodySmall)
                Text(text = state.status.name, style = MaterialTheme.typography.bodySmall)
            }
            Text(text = state.serviceInfo.type, style = MaterialTheme.typography.bodySmall)
        }
    }
}
