package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import einkaufszettel.db.ShopDto
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.components.CacheImage
import io.github.jan.einkaufszettel.common.ui.components.ShopCard
import io.github.jan.einkaufszettel.common.ui.events.AlertDialog
import io.github.jan.einkaufszettel.common.ui.icons.Add
import io.github.jan.einkaufszettel.common.ui.icons.Delete
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon

sealed interface SelectedShop {

    object None : SelectedShop

    object Create : SelectedShop

    @JvmInline
    value class Normal(val shop: ShopDto) : SelectedShop

    @JvmInline
    value class Edit(val shop: ShopDto) : SelectedShop

}

@Composable
fun ShoppingListScreen(viewModel: EinkaufszettelViewModel) {
    val shops by viewModel.shopFlow.collectAsState(listOf())
    var shopState by remember { mutableStateOf<SelectedShop>(SelectedShop.None) }
    val products by viewModel.productEntryFlow.collectAsState(listOf())
    AnimatedVisibility(
        visible = shopState is SelectedShop.Normal,
    ) {
        val shop = (shopState as? SelectedShop.Normal)?.shop ?: return@AnimatedVisibility
        val shopProducts = remember(products) { products.filter { it.shopId == shop.id } }
        ShopScreen(shopProducts, shop.id, viewModel) {
            shopState = SelectedShop.None
        }
    }
    AnimatedVisibility(
        visible = shopState is SelectedShop.None
    ) {
        LazyVerticalGrid(
            GridCells.Adaptive(150.dp),
        ) {
            items(shops, { it.id }) {
                Box(Modifier.padding(8.dp)) {
                    ShopCard(
                        shop = it,
                        onClick = {
                            shopState = SelectedShop.Normal(it)
                        },
                        onLongClick = {
                            shopState = SelectedShop.Edit(it)
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton({ shopState = SelectedShop.Create }, modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(100)) {
                Icon(LocalIcon.Add, "Erstellen")
            }
        }
    }

    AnimatedVisibility(
        visible = shopState is SelectedShop.Create
    ) {
        CreateScreen(
            viewModel = viewModel,
            create = { name, fileInfo, authorizedUsers ->
                viewModel.createShop(name, fileInfo!!, authorizedUsers)
                shopState = SelectedShop.None
            }
        ) {
            shopState = SelectedShop.None
        }
    }

    AnimatedVisibility(
        visible = shopState is SelectedShop.Edit
    ) {
        val shop = (shopState as? SelectedShop.Edit)?.shop ?: return@AnimatedVisibility
        var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
        CreateScreen(
            viewModel = viewModel,
            oldName = shop.name,
            oldAuthorizedUserIds = shop.authorizedUsers,
            oldImage = { CacheImage(shop.iconUrl, Modifier.size(100.dp)) },
            modifyImageEnabled = false,
            create = { name, _, authorizedUsers ->
                viewModel.editShop(shop.id, name, authorizedUsers)
                shopState = SelectedShop.None
            }
        ) {
            shopState = SelectedShop.None
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            FloatingActionButton(
                onClick = {
                    showDeleteDialog = true
                },
                modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(100),
            ) {
                Icon(LocalIcon.Delete, "Löschen")
            }
        }

        if(showDeleteDialog) {
            AlertDialog(
                message = "Möchtest du den Eintrag wirklich löschen?",
                confirmButton = {
                    Button(onClick = {
                        viewModel.deleteShop(shop.id, shop.iconUrl)
                        shopState = SelectedShop.None
                        showDeleteDialog = false
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
    }
}