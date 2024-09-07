package dev.nordix.homescreen.composable

import android.app.Presentation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nordix.service_manager.domain.model.ServiceState
import dev.nordix.service_manager.domain.model.resolved.ResolvedServiceState

@Composable
fun ServiceItem(
    modifier: Modifier = Modifier,
    isLast: Boolean,
    state: ServiceState,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            content = { Presentation(state) }
        )
        if (!isLast) HorizontalDivider(Modifier.padding(4.dp))
    }
}

@Composable
private fun RowScope.Presentation(state: ServiceState) {
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
