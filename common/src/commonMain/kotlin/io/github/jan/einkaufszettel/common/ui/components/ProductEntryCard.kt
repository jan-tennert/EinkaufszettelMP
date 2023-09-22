package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import einkaufszettel.db.GetAllEntries
import io.github.jan.einkaufszettel.common.ui.icons.Delete
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import kotlinx.datetime.toJavaInstant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ProductEntryCard(product: GetAllEntries, onDoneChange: (Boolean) -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp).combinedClickable(onLongClick = onEdit) {  }.animateItemPlacement()) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            val formattedDate = remember(product) { FORMATTER.format(LocalDateTime.ofInstant(product.createdAt.toJavaInstant(), ZoneOffset.systemDefault())) }
            if(false) {
                Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(30.dp))
                }
            } else {
                Checkbox(
                    checked = product.doneBy != null,
                    onCheckedChange = {
                        onDoneChange(it)
                    },
                )
            }
            Column {
                Text(text = product.content, modifier = Modifier.fillMaxWidth(0.9f), textDecoration = if(product.doneBy != null) TextDecoration.LineThrough else TextDecoration.None)
                Text(buildAnnotatedString {
                    append(formattedDate)
                    if(product.doneBy == null) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(" von ")
                        }
                        append(product.creator ?: "Unbekannt")
                    } else {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(" durchgestrichen von ")
                        }
                        append(product.doneBy ?: "Unbekannt")
                    }
                }, fontSize = 10.sp)
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                IconButton(onDelete) {
                    Icon(LocalIcon.Delete, "Löschen")
                }
            }
        }
    }
}