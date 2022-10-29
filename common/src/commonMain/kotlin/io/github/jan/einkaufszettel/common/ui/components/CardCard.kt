package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import einkaufszettel.db.GetAllCards
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.supabase.storage.storage

@Composable
fun CardImage(card: GetAllCards, viewModel: EinkaufszettelViewModel, modifier: Modifier = Modifier) {
    CacheImage(
        modifier = modifier,
        loadingFallback = {
            CircularProgressIndicator(modifier = Modifier.size(150.dp))
        },
        fileName = card.imagePath,
        produceImage = { viewModel.supabaseClient.storage["cards"].downloadAuthenticated(card.imagePath) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardCard(card: GetAllCards, viewModel: EinkaufszettelViewModel, modifier: Modifier = Modifier, onLongClick: () -> Unit, onClick: () -> Unit) {
    ElevatedCard(modifier = modifier.combinedClickable(onLongClick = onLongClick) { onClick() }) {
        Box(modifier = Modifier.padding(8.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CardImage(card, viewModel, Modifier.size(150.dp))
                Text(card.description)
            }
        }
    }
}