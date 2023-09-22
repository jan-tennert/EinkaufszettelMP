package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                                data = CacheData.Public(nutrition.product.imageUrl),
                                modifier = Modifier.size(70.dp),
                                loadingFallback = { CircularProgressIndicator(Modifier.size(70.dp)) })
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
                        items(nutrition.product.ingredients.distinct(), { it }) {
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
        Checkbox(checked = marked, enabled = false, onCheckedChange = {}, colors = CheckboxDefaults.colors(disabledCheckedColor = if(marked) Color.Green else Color.Red))
        Text(name, modifier = Modifier.padding(start = 7.dp))
    }
}