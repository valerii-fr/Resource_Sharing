package dev.nordix.common_ui.composable

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun NordixIconButton(
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    IconButton(onClick) {
        Icon(imageVector = imageVector, contentDescription = contentDescription)
    }
}
