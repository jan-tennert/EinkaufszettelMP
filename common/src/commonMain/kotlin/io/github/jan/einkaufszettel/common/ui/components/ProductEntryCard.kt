package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.events.AlertDialog
import io.github.jan.einkaufszettel.common.ui.icons.Delete
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import kotlinx.datetime.toJavaInstant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ProductEntryCard(product: GetAllEntries, viewModel: EinkaufszettelViewModel) {
    var loading by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp).combinedClickable(onLongClick = { showEditDialog = true }) {  }.animateItemPlacement()) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            val formattedDate = remember(product) { FORMATTER.format(LocalDateTime.ofInstant(product.createdAt.toJavaInstant(), ZoneOffset.systemDefault())) }
            if(loading) {
                Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(30.dp))
                }
            } else {
                Checkbox(
                    checked = product.doneBy != null,
                    onCheckedChange = {
                        loading = true
                        if(it) {
                            viewModel.markEntryAsDone(product.id) {
                                loading = false
                            }
                        } else {
                            viewModel.markEntryAsNotDone(product.id) {
                                loading = false
                            }
                        }
                    },
                )
            }
            Column {
                Text(text = product.content, textDecoration = if(product.doneBy != null) TextDecoration.LineThrough else null, modifier = Modifier.fillMaxWidth(0.9f))
                Text(buildAnnotatedString {
                    append(formattedDate)
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(" von ")
                    }
                    append(product.creator ?: "Unbekannt")
                }, fontSize = 10.sp)
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                IconButton({
                    showDeleteDialog = true
                }) {
                    Icon(LocalIcon.Delete, "Löschen")
                }
            }
        }
    }

    if(showDeleteDialog) {
        AlertDialog(
            message = "Möchtest du den Eintrag wirklich löschen?",
            confirmButton = {
                Button(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteEntry(product.id)
                }) {
                    Text("Löschen")
                }
                Button(
                    onClick = {
                        showDeleteDialog = false
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Abbrechen")
                }
            },
            close = {
                showDeleteDialog = false
            }
        )
    }

    if(showEditDialog) {
        EditEntryDialog(product.content, product.id, viewModel) {
            showEditDialog = false
        }
    }
}