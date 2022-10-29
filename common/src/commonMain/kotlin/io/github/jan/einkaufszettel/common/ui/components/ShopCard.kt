package io.github.jan.einkaufszettel.common.ui.components

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
import einkaufszettel.db.ShopDto

@Composable
fun ShopCard(shop: ShopDto, modifier: Modifier = Modifier, onClick: () -> Unit) {
    ElevatedCard(modifier = modifier, onClick = onClick) {
        Box(modifier = Modifier.padding(8.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CacheImage(url = shop.iconUrl, modifier = Modifier.size(150.dp)) {
                    CircularProgressIndicator(modifier = Modifier.size(150.dp))
                }
                Text(shop.name)
            }
        }
    }
}