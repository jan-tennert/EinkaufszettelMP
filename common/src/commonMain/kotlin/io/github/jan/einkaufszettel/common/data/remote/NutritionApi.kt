package io.github.jan.einkaufszettel.common.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ProductInfo(
    @SerialName("product_name")
    val productName: String,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("allergens")
    private val rawAllergens: String,
    @SerialName("allergens_from_ingredients")
    private val rawAllergensIngredients: String,
) {

    @Transient
    val isGlutenFree = "gluten" !in rawAllergens
    @Transient
    val isMilkFree = "milk" !in rawAllergens
    @Transient
    val isNutFree = "nuts" !in rawAllergens
    @Transient
    val ingredients = rawAllergensIngredients.split(",").drop(1)

}

@Serializable
data class NutritionData(
    val product: ProductInfo,
    val code: String
)

interface NutritionApi {

    suspend fun retrieveNutritionData(barcode: String): NutritionData?

}

internal class NutritionApiImpl(
    private val httpClient: HttpClient
): NutritionApi {

    override suspend fun retrieveNutritionData(barcode: String): NutritionData? {
        val response = httpClient.get("https://world.openfoodfacts.org/api/v2/product/3017620422003")
        return try {
            response.body<NutritionData>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}