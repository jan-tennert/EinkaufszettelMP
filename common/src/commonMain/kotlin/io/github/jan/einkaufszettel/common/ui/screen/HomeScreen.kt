package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.components.ProductEntryCard
import io.github.jan.einkaufszettel.common.ui.icons.Bookmark
import io.github.jan.einkaufszettel.common.ui.icons.BookmarkAdd
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import kotlinx.coroutines.flow.combine

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: EinkaufszettelViewModel) {
    val shops by viewModel.shopFlow.combine(viewModel.productEntryFlow) { shops, products ->
        shops
            .filter { // filter out shops that have no products
                products.any { product -> product.shopId == it.id }
            } // map the shops and products to a pair
            .sortedBy { it.isPinned }
            .map { it to products.filter { product -> product.shopId == it.id } }
    }.collectAsState(listOf())
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
                        if(shop.isPinned) {
                            Icon(LocalIcon.BookmarkAdd, null)
                        } else {
                            Icon(LocalIcon.Bookmark, null, tint = MaterialTheme.colorScheme.surfaceTint)
                        }
                    }
                }
            }
            if (shop.isVisible) {
                items(products, { it.id }) {
                    ProductEntryCard(it, viewModel)
                }
            }
        }
    }
}