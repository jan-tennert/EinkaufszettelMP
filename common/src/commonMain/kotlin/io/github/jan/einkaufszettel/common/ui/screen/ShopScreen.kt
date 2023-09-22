package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import einkaufszettel.db.GetAllEntries
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.components.ActionButton
import io.github.jan.einkaufszettel.common.ui.components.ProductEntryCard
import io.github.jan.einkaufszettel.common.ui.dialog.CreateEntryDialog
import io.github.jan.einkaufszettel.common.ui.dialog.EditEntryDialog
import io.github.jan.einkaufszettel.common.ui.events.AlertDialog
import io.github.jan.einkaufszettel.common.ui.icons.Add
import io.github.jan.einkaufszettel.common.ui.icons.ArrowBack
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon

@Composable
fun ShopScreen(products: List<GetAllEntries>, shopId: Long, viewModel: EinkaufszettelViewModel, close: () -> Unit) {
    var showEntryCreateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<GetAllEntries?>(null) }
    var showDeleteDialog by remember { mutableStateOf<GetAllEntries?>(null) }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)
    ) {
        items(products, { it.id }) { product ->
            ProductEntryCard(
                product = product,
                onDoneChange = {
                    if (it) {
                        viewModel.markEntryAsDone(product.id, {})
                    } else {
                        viewModel.markEntryAsNotDone(product.id, {})
                    }
                },
                onEdit = {
                //    viewModel.changeEntryName(product.id, it)
                },
                onDelete = {
                    viewModel.deleteEntry(product.id)
                }
            )
        }
    }

    ActionButton(Alignment.BottomStart, close) {
        Icon(LocalIcon.ArrowBack, "Zurück")
    }

    ActionButton(Alignment.BottomEnd, { showEntryCreateDialog = true }) {
        Icon(LocalIcon.Add, "Erstellen")
    }

    if(showEntryCreateDialog) {
        CreateEntryDialog(shopId, viewModel, close = { showEntryCreateDialog = false })
    }
    if(showDeleteDialog != null) {
        AlertDialog(
            message = "Möchtest du den Eintrag wirklich löschen?",
            confirmButton = {
                Button(onClick = {
                    showDeleteDialog = null
                    viewModel.deleteEntry(showDeleteDialog!!.id)
                }) {
                    Text("Löschen")
                }
                Button(
                    onClick = {
                        showDeleteDialog = null
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Abbrechen")
                }
            },
            close = {
                showDeleteDialog = null
            }
        )
    }
    if(showEditDialog != null) {
        EditEntryDialog(
            oldContent = showEditDialog!!.content,
            onEdit = {
                viewModel.editEntry(showEditDialog!!.id, it)
            }
        ) {
            showEditDialog = null
        }
    }
}