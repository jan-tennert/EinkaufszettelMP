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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import einkaufszettel.db.GetAllRecipes
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import io.github.jan.einkaufszettel.common.ui.icons.QuestionMark
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.storage.storage

@Composable
fun RecipeCard(
    recipe: GetAllRecipes,
    modifier: Modifier,
    viewModel: EinkaufszettelViewModel,
    onClick: () -> Unit = {}
) {
    ElevatedCard(onClick, modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            var text by rememberSaveable { mutableStateOf(recipe.name) }
            Text(
                text,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    // the following causes a recomposition if there isn't enough text to occupy the minimum number of lines!
                    if ((textLayoutResult.lineCount) < 2) {
                        // don't forget the space after the line break, otherwise empty lines won't get full height!
                        text = recipe.name + "\n ".repeat(2 - textLayoutResult.lineCount)
                    }
                },
            )
            if (recipe.imagePath != null) {
                val url = remember(recipe) { viewModel.supabaseClient.storage["recipes"].authenticatedUrl(recipe.imagePath) }
                CacheImage(
                    data = CacheData.Authenticated(url, viewModel.supabaseClient.gotrue.currentAccessTokenOrNull() ?: ""),
                    modifier = Modifier.size(90.dp).padding(8.dp),
                    loadingFallback = {
                        CircularProgressIndicator(modifier = Modifier.size(90.dp).padding(8.dp))
                    },
                    size = CacheSize(90, 90)
                )
            } else {
                Icon(
                    LocalIcon.QuestionMark,
                    contentDescription = null,
                    modifier = Modifier.size(90.dp).padding(8.dp)
                )
            }
        }
    }
}