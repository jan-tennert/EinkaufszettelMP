package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import einkaufszettel.db.GetAllRecipes
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.remote.FileInfo
import io.github.jan.einkaufszettel.common.toComposeImage
import io.github.jan.einkaufszettel.common.ui.components.ActionButton
import io.github.jan.einkaufszettel.common.ui.components.ImageChooser
import io.github.jan.einkaufszettel.common.ui.components.IngredientCreateItem
import io.github.jan.einkaufszettel.common.ui.dialog.IngredientDialog
import io.github.jan.einkaufszettel.common.ui.events.UIEvent
import io.github.jan.einkaufszettel.common.ui.icons.Done
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import io.github.jan.einkaufszettel.common.ui.icons.QuestionMark

@Composable
fun RecipeCreateScreen(placeholder: GetAllRecipes? = null, viewModel: EinkaufszettelViewModel, create: (String, FileInfo?, String, List<String>, Boolean) -> Unit, back: () -> Unit) {
    BackHandle(back)
    val scrollState = rememberScrollState()
    var showIngredientDialog by remember { mutableStateOf(false) }
    var showStepDialog by remember { mutableStateOf(false) }
    val ingredients = remember { mutableStateListOf(*(placeholder?.ingredients ?: emptyList<String>()).toTypedArray()) }
    var private by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
       // verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var name by remember { mutableStateOf(placeholder?.name ?: "") }
        val steps = remember { mutableStateOf(placeholder?.steps ?: "") }
        var fileInfo by remember { mutableStateOf<FileInfo?>(null) }
        val imageBitmap by remember(fileInfo) {
            if(fileInfo != null) {
                mutableStateOf(fileInfo!!.bytes.toComposeImage())
            } else {
                mutableStateOf(null)
            }
        }
        var showImageChooser by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = false
        )
        BoxWithConstraints(
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
        ) {
            var maxLines: Int by remember { mutableStateOf(Int.MAX_VALUE) }
            OutlinedTextField(
                value = steps.value,
                onValueChange = { steps.value = it },
                label = { Text("Anleitung") },
                maxLines = maxLines,
                modifier = Modifier
                    .onFocusChanged {
                        maxLines = if (!it.isFocused) {
                            2
                        } else {
                            Int.MAX_VALUE
                        }
                    }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.padding(10.dp).let { it.clickable { showImageChooser = true } }) {
                //if(oldImage != null) {
                //oldImage()
                /*   } else*/ if(imageBitmap == null) {
                Icon(
                    LocalIcon.QuestionMark, "", modifier = Modifier
                        .size(100.dp)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.onBackground
                        ),
                    tint = MaterialTheme.colorScheme.onBackground)
            } else {
                Image(
                    imageBitmap!!, "", modifier = Modifier
                        .size(100.dp)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.onBackground
                        ))
            }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(private, { private = it })
                Text("Privat")
            }
        }
        ImageChooser(showImageChooser) {
            fileInfo = it
            showImageChooser = false
        }
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            items(ingredients, { it} ) {
                IngredientCreateItem(it) {
                    ingredients.remove(it)
                }
            }
        }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Button({
                showIngredientDialog = true
            }) {
                Text("Zutat hinzufügen")
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            ActionButton(
                location = Alignment.BottomEnd,
                onClick = {
                    if (name.isNotBlank() && ingredients.isNotEmpty()) {
                        create(name, fileInfo, steps.value, ingredients.toList(), private)
                        back()
                    } else {
                        viewModel.events.add(UIEvent.Alert("Bitte gib einen Namen und mindestens eine Zutat an"))
                    }
                }
            ) {
                Icon(LocalIcon.Done, "Fertig")
            }
        }
    }


    if(showIngredientDialog) {
        IngredientDialog({
            if(it in ingredients) {
                viewModel.events.add(UIEvent.Alert("Zutat bereits vorhanden"))
            } else {
                ingredients.add(it)
            }
        }) {
            showIngredientDialog = false
        }
    }

}