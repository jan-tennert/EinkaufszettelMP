package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import einkaufszettel.db.LocalUserDto
import io.github.jan.einkaufszettel.common.ui.icons.Delete
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.UserCard(localUserDto: LocalUserDto, checked: Boolean, delete: () -> Unit, onCheck: (Boolean) -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(8.dp).animateItemPlacement()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheck
            )
            Text(text = localUserDto.username, modifier = Modifier.padding(10.dp))
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        delete()
                    },
                    ) {
                    Icon(LocalIcon.Delete, "Löschen", tint = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}