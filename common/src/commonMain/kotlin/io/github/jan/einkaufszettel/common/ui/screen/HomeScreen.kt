package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.components.ProductEntryCard
import kotlinx.coroutines.flow.combine

@Composable
fun HomeScreen(viewModel: EinkaufszettelViewModel) {
    val shops by viewModel.shopFlow.combine(viewModel.productEntryFlow) { shops, products ->
        shops
            .filter { // filter out shops that have no products
                products.any { product -> product.shopId == it.id }
            } // map the shops and products to a pair
            .map { it to products.filter { product -> product.shopId == it.id } }
    }.collectAsState(listOf())
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        shops.forEach { (shop, products) ->
            item {
                Text(shop.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            items(products, { it.id }) {
                ProductEntryCard(it, viewModel)
            }
        }
    }
}