package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import einkaufszettel.db.ShopDto
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.components.ActionButton
import io.github.jan.einkaufszettel.common.ui.components.CacheImage
import io.github.jan.einkaufszettel.common.ui.components.ShopCard
import io.github.jan.einkaufszettel.common.ui.dialog.DeleteDialog
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
        BackHandle {
            shopState = SelectedShop.None
        }
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
                Box(Modifier
                    .padding(8.dp)
                ) {
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

        ActionButton(Alignment.BottomEnd, { shopState = SelectedShop.Create }, {
            Icon(LocalIcon.Add, "Erstellen")
        })
    }

    AnimatedVisibility(
        visible = shopState is SelectedShop.Create
    ) {
        BackHandle {
            shopState = SelectedShop.None
        }
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
        BackHandle {
            shopState = SelectedShop.None
        }
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

        ActionButton(Alignment.BottomCenter, { showDeleteDialog = true }, {
            Icon(LocalIcon.Delete, "Löschen")
        })

        if (showDeleteDialog) {
            DeleteDialog(
                onDismiss = { showDeleteDialog = false },
                onDelete = {
                    viewModel.deleteShop(shop.id, shop.iconUrl)
                    shopState = SelectedShop.None
                }
            )
        }
    }
}