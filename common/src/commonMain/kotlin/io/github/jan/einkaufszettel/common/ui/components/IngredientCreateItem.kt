package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.ui.icons.Delete
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon

@Composable
fun IngredientCreateItem(text: String, edit: () -> Unit, onDelete: () -> Unit) {
    ElevatedCard(edit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text, modifier = Modifier.padding(8.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                IconButton(onDelete) {
                    Icon(LocalIcon.Delete, "")
                }
            }
        }
    }
}