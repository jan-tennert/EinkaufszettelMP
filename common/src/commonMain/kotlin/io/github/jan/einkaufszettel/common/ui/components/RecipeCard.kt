package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import einkaufszettel.db.GetAllRecipes
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import io.github.jan.einkaufszettel.common.ui.icons.QuestionMark
import io.github.jan.supabase.storage.storage

@Composable
fun RecipeCard(recipe: GetAllRecipes, modifier: Modifier, viewModel: EinkaufszettelViewModel, onClick: () -> Unit = {}) {
    ElevatedCard(onClick, modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(recipe.name.take(20) + if (recipe.name.length > 20) "..." else "")
            if(recipe.imagePath != null) {
                CacheImage(recipe.imagePath, produceImage = {
                    viewModel.supabaseClient.storage["recipes"].downloadAuthenticated(recipe.imagePath)
                }, modifier = Modifier.size(90.dp).padding(8.dp), loadingFallback = {
                    CircularProgressIndicator(modifier = Modifier.size(90.dp).padding(8.dp))
                })
            } else {
                Icon(LocalIcon.QuestionMark, contentDescription = null, modifier = Modifier.size(90.dp).padding(8.dp))
            }
        }
    }
}