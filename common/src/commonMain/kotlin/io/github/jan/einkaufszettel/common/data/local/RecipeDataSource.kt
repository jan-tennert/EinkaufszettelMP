package io.github.jan.einkaufszettel.common.data.local

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import einkaufszettel.db.GetAllRecipes
import io.github.jan.einkaufszettel.common.data.remote.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeDataSource {

    suspend fun insertRecipe(recipe: Recipe)

    fun getRecipes(): Flow<List<GetAllRecipes>>

    suspend fun deleteRecipeById(id: Long)

    suspend fun insertAll(recipes: List<Recipe>)

}

internal class RecipeDataSourceImpl(
    db: EinkaufszettelDatabase
): RecipeDataSource {

    private val queries = db.recipeDtoQueries

    override suspend fun deleteRecipeById(id: Long) {
        queries.deleteRecipeById(id)
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        queries.insertRecipe(
            createdAt = recipe.createdAt,
            creatorId = recipe.creatorId,
            imagePath = recipe.imagePath,
            name = recipe.name,
            ingredients = recipe.ingredients,
            steps = recipe.steps,
            id = recipe.id.toLong(),
            isPrivate = recipe.private
        )
    }

    override fun getRecipes(): Flow<List<GetAllRecipes>> {
        return queries.getAllRecipes().asFlow().mapToList()
    }

    override suspend fun insertAll(recipes: List<Recipe>) {
        val oldData = queries.getAllRecipes().executeAsList()
        queries.transaction {
            val toDelete = oldData.filter { recipes.none { newRecipe -> newRecipe.id.toLong() == it.id } }
            recipes.forEach { recipe ->
                queries.insertRecipe(
                    createdAt = recipe.createdAt,
                    creatorId = recipe.creatorId,
                    imagePath = recipe.imagePath,
                    name = recipe.name,
                    ingredients = recipe.ingredients,
                    steps = recipe.steps,
                    id = recipe.id.toLong(),
                    isPrivate = recipe.private
                )
            }
            toDelete.forEach { queries.deleteRecipeById(it.id) }
        }
    }

}