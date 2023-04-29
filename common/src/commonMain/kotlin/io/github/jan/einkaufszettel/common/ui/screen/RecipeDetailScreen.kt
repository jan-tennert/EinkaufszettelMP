package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import einkaufszettel.db.GetAllRecipes
import einkaufszettel.db.ShopDto
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.ui.components.CacheImage
import io.github.jan.einkaufszettel.common.ui.components.IngredientItem
import io.github.jan.einkaufszettel.common.ui.dialog.CreateEntryDialog
import io.github.jan.einkaufszettel.common.ui.dialog.DeleteDialog
import io.github.jan.einkaufszettel.common.ui.dialog.Dialog
import io.github.jan.einkaufszettel.common.ui.icons.Delete
import io.github.jan.einkaufszettel.common.ui.icons.Edit
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.storage.storage

@Composable
fun RecipeDetailScreen(recipe: GetAllRecipes, viewModel: EinkaufszettelViewModel, onClose: () -> Unit = {}) {
    BackHandle(onClose)

    var showSteps by remember { mutableStateOf(false) }
    var showIngredients by remember { mutableStateOf(false) }
    val shops by viewModel.shopFlow.collectAsState(listOf())
    val darkMode by viewModel.darkMode.collectAsState(EinkaufszettelSettings.DarkMode.NOT_SET)
    val isOwner = viewModel.supabaseClient.gotrue.currentSessionOrNull()?.user?.id == recipe.creatorId
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        Text(recipe.name, modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.displaySmall)
        recipe.imagePath?.let {
            CacheImage(it, produceImage = {
                viewModel.supabaseClient.storage["recipes"].downloadAuthenticated(recipe.imagePath)
            }, modifier = Modifier.padding(8.dp))
        }
        Spacer(Modifier.weight(1f))
        recipe.steps?.let {
            Button({
                showSteps = true
            }) {
                Text("Anleitung anzeigen")
            }
        }
        Button({
            showIngredients = true
        }) {
            Text("Zutaten anzeigen")
        }
    }
    if(showSteps) {
        Dialog({ showSteps = false}, title = "Anleitung", darkMode = darkMode) {
            Text(recipe.steps!!, Modifier.background(MaterialTheme.colorScheme.background, RoundedCornerShape(20)).padding(10.dp).verticalScroll(
                rememberScrollState()
            ))
        }
    }
    if(showIngredients) {
        var showEntryCreateDialog by remember { mutableStateOf<Pair<ShopDto, String>?>(null) }
        Dialog({ showIngredients = false }, title = "Zutaten", darkMode = darkMode) {
            LazyColumn {
                items(recipe.ingredients) { ingredient ->
                    IngredientItem(ingredient, shops) {
                        showEntryCreateDialog = it to ingredient
                    }
                }
            }
        }
        if(showEntryCreateDialog != null) {
            val (shop, ingredient) = showEntryCreateDialog!!
            CreateEntryDialog(shop.id, viewModel, ingredient) {
                showEntryCreateDialog = null
            }
        }
    }
    if(isOwner) {
        var showDeleteDialog by remember { mutableStateOf(false) }
        var showEditScreen by remember { mutableStateOf(false) }
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton({
                showDeleteDialog = true
            }, modifier = Modifier.padding(8.dp)) {
                Icon(LocalIcon.Delete, null)
            }
        }
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton({
                showEditScreen = true
            }, modifier = Modifier.padding(8.dp)) {
                Icon(LocalIcon.Edit, null)
            }
        }

        if(showDeleteDialog) {
            DeleteDialog({
                viewModel.deleteRecipe(recipe.id)
                onClose()
            }) {
                showDeleteDialog = false
            }
        }
        if(showEditScreen) {
            Surface(Modifier.fillMaxSize()) {
                RecipeCreateScreen(recipe, viewModel, { name, file, steps, ingr, private ->
                    viewModel.updateRecipe(recipe.id, name, file, steps, ingr, private)
                    onClose()
                }) {
                    showEditScreen = false
                }
            }
        }
    }
}