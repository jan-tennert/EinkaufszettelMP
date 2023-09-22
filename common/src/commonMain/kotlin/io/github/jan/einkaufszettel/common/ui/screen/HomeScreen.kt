package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import einkaufszettel.db.GetAllEntries
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.components.ProductEntryCard
import io.github.jan.einkaufszettel.common.ui.dialog.EditEntryDialog
import io.github.jan.einkaufszettel.common.ui.events.AlertDialog
import io.github.jan.einkaufszettel.common.ui.icons.Bookmark
import io.github.jan.einkaufszettel.common.ui.icons.BookmarkAdd
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: EinkaufszettelViewModel) {
    val shops by viewModel.homeFlow.collectAsState(listOf())
    var showEditDialog by remember { mutableStateOf<GetAllEntries?>(null) }
    var showDeleteDialog by remember { mutableStateOf<GetAllEntries?>(null) }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        shops.forEach { (shop, products) ->
            item {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .animateItemPlacement()
                        .clickable {
                            viewModel.changeShopVisibility(shop.id, !shop.isVisible)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = shop.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    IconButton(
                        onClick = {
                            viewModel.changeShopPinned(shop.id, !shop.isPinned)
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        if (shop.isPinned) {
                            Icon(LocalIcon.BookmarkAdd, null)
                        } else {
                            Icon(
                                LocalIcon.Bookmark,
                                null,
                                tint = MaterialTheme.colorScheme.surfaceTint
                            )
                        }
                    }
                }
            }
            if (shop.isVisible) {
                items(products, { it.id }) { product ->
                    ProductEntryCard(
                        product = product,
                        onDoneChange = {
                            if (it) {
                                viewModel.markEntryAsDone(product.id)
                            } else {
                                viewModel.markEntryAsNotDone(product.id)
                            }
                        },
                        onEdit = {
                            showEditDialog = product
                        },
                        onDelete = {
                            showDeleteDialog = product
                        }
                    )
                }
            }
        }
    }
    if(showDeleteDialog != null) {
        AlertDialog(
            message = "Möchtest du den Eintrag wirklich löschen?",
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteEntry(showDeleteDialog!!.id)
                    showDeleteDialog = null
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