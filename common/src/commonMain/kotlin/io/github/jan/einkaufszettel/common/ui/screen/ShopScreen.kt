package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import einkaufszettel.db.GetAllEntries
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.components.CreateEntryDialog
import io.github.jan.einkaufszettel.common.ui.components.ProductEntryCard
import io.github.jan.einkaufszettel.common.ui.icons.Add
import io.github.jan.einkaufszettel.common.ui.icons.ArrowBack
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon

@Composable
fun ShopScreen(products: List<GetAllEntries>, shopId: Long, viewModel: EinkaufszettelViewModel, close: () -> Unit) {
    var showEntryCreateDialog by remember { mutableStateOf(false) }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        items(products, { it.id }) {
            ProductEntryCard(it, viewModel)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        FloatingActionButton(close, modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(100)) {
            Icon(LocalIcon.ArrowBack, "Zurück")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton({ showEntryCreateDialog = true }, modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(100)) {
            Icon(LocalIcon.Add, "Erstellen")
        }
    }

    AnimatedVisibility(showEntryCreateDialog) {
        CreateEntryDialog(shopId, viewModel, close = { showEntryCreateDialog = false })
    }
}