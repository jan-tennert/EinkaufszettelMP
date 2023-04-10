package io.github.jan.einkaufszettel.common.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Recipe(
    val id: Int,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("creator_id")
    val creatorId: String,
    @SerialName("image_path")
    val imagePath: String,
    val name: String,
    val ingredients: List<String>,
    val steps: String,
    val private: Boolean
)

interface RecipeApi {

    suspend fun createRecipe(name: String, creatorId: String, image: FileInfo?, ingredients: List<String>, steps: String?, private: Boolean): Recipe

    suspend fun editRecipe(id: Int, name: String, image: FileInfo?, ingredients: List<String>, steps: String?, private: Boolean): Recipe

    suspend fun retrieveRecipes() : List<Recipe>

    suspend fun deleteRecipe(id: Int)

}

internal class RecipeApiImpl(
    supabaseClient: SupabaseClient
): RecipeApi {

    private val table = supabaseClient.postgrest["recipes"]
    private val bucket = supabaseClient.storage["recipes"]

    override suspend fun createRecipe(
        name: String,
        creatorId: String,
        image: FileInfo?,
        ingredients: List<String>,
        steps: String?,
        private: Boolean
    ): Recipe {
        val imagePath = image?.let { bucket.upload(Clock.System.now().toEpochMilliseconds().toString() + "." + image.extension, image.bytes).substringAfterLast("/") }
        return table.insert(buildJsonObject {
            put("name", name)
            put("creator_id", creatorId)
            put("image_path", imagePath)
            put("ingredients", JsonArray(ingredients.map { JsonPrimitive(it) }))
            put("steps", steps)
            put("private", private)
        }).decodeSingle()
    }

    override suspend fun editRecipe(
        id: Int,
        name: String,
        image: FileInfo?,
        ingredients: List<String>,
        steps: String?,
        private: Boolean
    ): Recipe {
        val imagePath = image?.let { bucket.upload(Clock.System.now().toEpochMilliseconds().toString() + "." + image.extension, image.bytes).substringAfterLast("/") }
        return table.update(
            {
                Recipe::name setTo name
                Recipe::imagePath setTo imagePath
                Recipe::ingredients setTo ingredients
                Recipe::steps setTo steps
            }
        ) {
            Recipe::id eq id
        }.decodeSingle()
    }

    override suspend fun deleteRecipe(id: Int) {
        table.delete { Recipe::id eq id }
    }

    override suspend fun retrieveRecipes(): List<Recipe> {
        return table.select().decodeList()
    }

}