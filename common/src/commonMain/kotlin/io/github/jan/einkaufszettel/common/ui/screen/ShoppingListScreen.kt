package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import einkaufszettel.db.ShopDto
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.local.ShopDtpSerializer
import io.github.jan.einkaufszettel.common.ui.components.ShopCard
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
fun ShoppingListScreen(viewModel: EinkaufszettelViewModel) {
    val shops by viewModel.shopFlow.collectAsState(listOf())
    val shopSaver = remember { Saver<MutableState<ShopDto?>, String>(
        save = { it.value?.let { shop -> Json.encodeToString(ShopDtpSerializer, shop) } },
        restore = { mutableStateOf(try { Json.decodeFromString(it) } catch(e: Exception) { null }) }
    ) }
    var selectedShop by rememberSaveable(saver = shopSaver) { mutableStateOf(null) }
    val products by viewModel.productEntryFlow.collectAsState(listOf())
    AnimatedVisibility(
        visible = selectedShop != null,
    ) {
        val shopProducts = remember(products) { products.filter { it.shopId == selectedShop?.id } }
        ShopScreen(shopProducts, selectedShop!!.id, viewModel) {
            selectedShop = null
        }
    }
    AnimatedVisibility(
        visible = selectedShop == null
    ) {
        LazyVerticalGrid(
            GridCells.Adaptive(150.dp),
        ) {
            items(shops, { it.id }) {
                Box(Modifier.padding(8.dp)) {
                    ShopCard(it) {
                        selectedShop = it
                    }
                }
            }
        }
    }
}