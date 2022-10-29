package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.jan.einkaufszettel.common.data.remote.NutritionData

@Composable
fun NutritionCard(nutrition: NutritionData, close: () -> Unit) {
    var showAllergens by remember { mutableStateOf(false) }

    if (!showAllergens) {
            Dialog(close) {
                ElevatedCard {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(30.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CacheImage(
                                nutrition.product.imageUrl,
                                modifier = Modifier.size(70.dp),
                                { CircularProgressIndicator(Modifier.size(70.dp)) })
                            Text(
                                nutrition.product.productName,
                                modifier = Modifier.padding(start = 7.dp)
                            )
                        }
                        Column {
                            NutritionMarker("Glutenfrei", nutrition.product.isGlutenFree)
                            NutritionMarker("Milchfrei", nutrition.product.isMilkFree)
                            NutritionMarker("Nussfrei", nutrition.product.isNutFree)
                        }

                        Button(onClick = { showAllergens = true }) {
                            Text("Allergene anzeigen")
                        }
                    }
                }
            }
        } else {
            Dialog({ showAllergens = false }) {
                ElevatedCard {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp),
                    ) {
                        items(nutrition.product.ingredients, { it }) {
                            Text(it, modifier = Modifier.padding(top = 5.dp))
                        }
                    }
                }
            }
        }
    }

@Composable
fun NutritionMarker(name: String, marked: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = marked, enabled = false, onCheckedChange = {})
        Text(name, modifier = Modifier.padding(start = 7.dp))
    }
}