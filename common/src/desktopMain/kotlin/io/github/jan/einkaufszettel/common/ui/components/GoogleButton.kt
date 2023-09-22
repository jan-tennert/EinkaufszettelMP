package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.icons.resourceIcon

@Composable
actual fun GoogleButton(
    modifier: Modifier,
    text: String,
    viewModel: EinkaufszettelViewModel
) {
    OutlinedButton({ viewModel.loginWithGoogle() }, modifier) {
        Icon(
            painter = resourceIcon("google_logo.xml"),
            contentDescription = "Google Button",
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}