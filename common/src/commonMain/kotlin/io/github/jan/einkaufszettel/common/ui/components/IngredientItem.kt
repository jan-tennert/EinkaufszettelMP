package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import einkaufszettel.db.ShopDto
import io.github.jan.einkaufszettel.common.ui.icons.Add
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon

@Composable
fun IngredientItem(ingredient: String, shops: List<ShopDto>, onAdd: (ShopDto) -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    ElevatedCard(Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(ingredient)
            Spacer(Modifier.weight(1f))
            Box {
                IconButton({ expanded = true }) {
                    Icon(LocalIcon.Add, null)
                }
                DropdownMenu(expanded, { expanded = false }) {
                    shops.forEach {
                        DropdownMenuItem(text = { Text(it.name) }, {
                            onAdd(it)
                            expanded = false
                        })
                    }
                }
            }
        }
    }
}