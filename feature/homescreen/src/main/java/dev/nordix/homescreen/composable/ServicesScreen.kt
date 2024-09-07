package dev.nordix.homescreen.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.nordix.homescreen.model.ServiceActionsWrapper
import dev.nordix.services.domain.model.ActionsWrapper
import dev.nordix.services.domain.model.actions.ServiceAction
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

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
            style = MaterialTheme.typography.labelLarge
        )
        Card(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
            )
        ) {
            actionsWrapper.actions.forEach { action ->
                ActionItem(
                    action = action,
                    onAction = onAction
                )
            }
        }
        HorizontalDivider()
    }
}

@Composable
private fun ActionItem(
    action: KClass<ServiceAction<*>>,
    onAction: (ServiceAction<*>) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(4.dp)
    ) {
        action.primaryConstructor?.let { constructor ->
            val fieldValues = remember { mutableStateMapOf<String, Any?>() }
            val parameters = constructor.parameters
            parameters.forEach { parameter ->
                val paramName = parameter.name ?: "Unknown"
                val paramType = parameter.type.classifier as? KClass<*>
                when (paramType) {
                    String::class -> {
                        TextField(
                            value = fieldValues[paramName] as String? ?: "",
                            onValueChange = { newValue ->
                                fieldValues[paramName] = newValue
                            },
                            label = { Text(paramName) }
                        )
                    }
                    Number::class -> {
                        TextField(
                            value = (fieldValues[paramName] as Number?)?.toString() ?: "",
                            onValueChange = { newValue ->
                                fieldValues[paramName] = newValue.toIntOrNull()
                            },
                            label = { Text(paramName) },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }


            Button(onClick = {
                val params = parameters.map { param ->
                    fieldValues[param.name] ?: getDefaultForType(param.type.classifier as? KClass<*>)
                }.toTypedArray()

                val actionInstance = constructor.call(*params)
                onAction(actionInstance)
            }) {
                Text(text = "Create ${action.simpleName}")
            }

        } ?: run {
            Button(onClick = { action.objectInstance?.let { onAction(it) } }) {
                Text(text = action.simpleName.toString())
            }
        }
    }
}

fun getDefaultForType(kClass: KClass<*>?): Any? {
    return when (kClass) {
        String::class -> ""
        Int::class -> 0
        else -> null
    }
}