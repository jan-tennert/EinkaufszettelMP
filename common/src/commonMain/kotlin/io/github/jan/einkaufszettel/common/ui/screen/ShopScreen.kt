package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import einkaufszettel.db.GetAllEntries
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.components.ActionButton
import io.github.jan.einkaufszettel.common.ui.components.ProductEntryCard
import io.github.jan.einkaufszettel.common.ui.dialog.CreateEntryDialog
import io.github.jan.einkaufszettel.common.ui.icons.Add
import io.github.jan.einkaufszettel.common.ui.icons.ArrowBack
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon

@Composable
fun ShopScreen(products: List<GetAllEntries>, shopId: Long, viewModel: EinkaufszettelViewModel, close: () -> Unit) {
    var showEntryCreateDialog by remember { mutableStateOf(false) }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)
    ) {
        items(products, { it.id }) {
            ProductEntryCard(it, viewModel)
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
}