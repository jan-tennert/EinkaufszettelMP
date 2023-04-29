package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.components.ActionButton
import io.github.jan.einkaufszettel.common.ui.components.RecipeCard
import io.github.jan.einkaufszettel.common.ui.icons.Add
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon

@Composable
fun RecipeScreen(viewModel: EinkaufszettelViewModel) {
    var showCreateScreen by remember { mutableStateOf(false) }
    val recipes by viewModel.recipeFlow.collectAsState(listOf())
    var recipeDetail by remember { mutableStateOf<Int?>(null) }
    if(showCreateScreen) {
        RecipeCreateScreen(null, viewModel, { name, file, steps, ingr, private ->
            viewModel.createRecipe(name, file, steps, ingr, private)
        }) { showCreateScreen = false }
    } else if(recipeDetail != null) {
        RecipeDetailScreen(remember(recipes) { recipes.first { re -> re.id.toInt() == recipeDetail }}, viewModel) { recipeDetail = null }
    } else {
        Column {
            var search by remember { mutableStateOf("") }
            val actualRecipes = if(search.isNotBlank()) {
                recipes.filter { it.name.contains(search, true) }
            } else {
                recipes
            }
            TextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Suche") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            LazyVerticalGrid(GridCells.Adaptive(100.dp)) {
                items(actualRecipes, { it.id }) {
                    RecipeCard(it, Modifier.padding(8.dp), viewModel) {
                        recipeDetail = it.id.toInt()
                    }
                }
            }
        }
        ActionButton(Alignment.BottomEnd, { showCreateScreen = true }) {
            Icon(LocalIcon.Add, "Erstellen")
        }
    }

}