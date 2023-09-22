package io.github.jan.einkaufszettel.common.data.local

import io.github.jan.einkaufszettel.common.data.remote.Card
import io.github.jan.einkaufszettel.common.data.remote.ProductEntry
import io.github.jan.einkaufszettel.common.data.remote.Recipe
import io.github.jan.einkaufszettel.common.data.remote.RemoteUser
import io.github.jan.einkaufszettel.common.data.remote.Shop

interface RootDataSource {

    suspend fun insertAll(
        shops: List<Shop>,
        cards: List<Card>,
        products: List<ProductEntry>,
        users: List<RemoteUser>,
        recipes: List<Recipe>
    )

}

internal class RootDataSourceImpl(
    private val database: EinkaufszettelDatabase
): RootDataSource {

    override suspend fun insertAll(
        shops: List<Shop>,
        cards: List<Card>,
        products: List<ProductEntry>,
        users: List<RemoteUser>,
        recipes: List<Recipe>
    ) {
        val oldShops = database.shopDtoQueries.getAllShops().executeAsList()
        val oldProducts = database.productEntryQueries.getAllEntries().executeAsList()
        val oldCards = database.cardDtoQueries.getAllCards().executeAsList()
        val oldRecipes = database.recipeDtoQueries.getAllRecipes().executeAsList()
        database.transaction {
            users.forEach {
                database.localUserDtoQueries.insertUser(
                    id = it.id,
                    username = it.username
                )
            }

            cards.forEach { card ->
                database.cardDtoQueries.insertCard(
                    createdAt = card.createdAt,
                    ownerId = card.ownerId,
                    imagePath = card.imagePath,
                    description = card.description,
                    authorizedUsers = card.authorizedUsers ?: emptyList(),
                    id = card.id.toLong()
                )
            }

            products.forEach { entry ->
                database.productEntryQueries.insertEntry(
                    id = entry.id.toLong(),
                    content = entry.content,
                    createdAt = entry.createdAt,
                    shopId = entry.shopId.toLong(),
                    doneById = entry.doneById,
                    creatorId = entry.creatorId
                )
            }

            shops.forEach {
                val oldShop = oldShops.find { oldShop -> oldShop.id == it.id.toLong() }
                database.shopDtoQueries.insertShop(
                    id = it.id.toLong(),
                    name = it.name,
                    createdAt = it.createdAt,
                    iconUrl = it.iconUrl,
                    ownerId = it.ownerId,
                    authorizedUsers = it.authorizedUsers,
                    isVisible = oldShop?.isVisible ?: true,
                    isPinned = oldShop?.isPinned ?: true
                )
            }

            recipes.forEach { recipe ->
                database.recipeDtoQueries.insertRecipe(
                    id = recipe.id.toLong(),
                    createdAt = recipe.createdAt,
                    creatorId = recipe.creatorId,
                    imagePath = recipe.imagePath,
                    name = recipe.name,
                    ingredients = recipe.ingredients,
                    steps = recipe.steps,
                    isPrivate = recipe.private
                )
            }

            val toDeleteRecipes = oldRecipes.filter { recipes.none { newRecipe -> newRecipe.id.toLong() == it.id } }
            toDeleteRecipes.forEach { database.recipeDtoQueries.deleteRecipeById(it.id) }

            val toDelete = oldShops.filter { shops.none { newShop -> newShop.id.toLong() == it.id } }
            toDelete.forEach { database.shopDtoQueries.deleteShopById(it.id) }

            val toDeleteProducts = oldProducts.filter { products.none { newProduct -> newProduct.id.toLong() == it.id } }
            toDeleteProducts.forEach { database.productEntryQueries.deleteEntryById(it.id) }

            val toDeleteCards = oldCards.filter { cards.none { newCard -> newCard.id.toLong() == it.id } }
            toDeleteCards.forEach { database.cardDtoQueries.deleteCardById(it.id) }
        }
    }

}