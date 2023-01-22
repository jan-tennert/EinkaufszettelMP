package io.github.jan.einkaufszettel.common.data.local

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import einkaufszettel.db.GetAllCards
import io.github.jan.einkaufszettel.common.data.remote.Card
import io.github.jan.einkaufszettel.common.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface CardDataSource {

    suspend fun insertCard(card: Card)

    fun getCards(): Flow<List<GetAllCards>>

    suspend fun deleteCardById(id: Long)

    suspend fun insertAll(cards: List<Card>)

}

internal class CardDataSourceImpl(
    db: EinkaufszettelDatabase
) : CardDataSource {

    private val queries = db.cardDtoQueries

    override suspend fun deleteCardById(id: Long) {
        withContext(ioDispatcher) {
            queries.deleteCardById(id)
        }
    }

    override suspend fun insertCard(card: Card) {
        withContext(ioDispatcher) {
            queries.insertCard(
                createdAt = card.createdAt,
                ownerId = card.ownerId,
                imagePath = card.imagePath,
                description = card.description,
                authorizedUsers = card.authorizedUsers ?: emptyList(),
                id = card.id.toLong()
            )
        }
    }

    override fun getCards(): Flow<List<GetAllCards>> {
        return queries.getAllCards().asFlow().mapToList()
    }


    override suspend fun insertAll(cards: List<Card>) {
        withContext(ioDispatcher) {
            val oldData = queries.getAllCards().executeAsList()
            queries.transaction {
                val toDelete = oldData.filter { cards.none { newCard -> newCard.id.toLong() == it.id } }
                cards.forEach { card ->
                    queries.insertCard(
                        createdAt = card.createdAt,
                        ownerId = card.ownerId,
                        imagePath = card.imagePath,
                        description = card.description,
                        authorizedUsers = card.authorizedUsers ?: emptyList(),
                        id = card.id.toLong()
                    )
                }
                toDelete.forEach {
                    queries.deleteCardById(it.id)
                }
            }
        }
    }

}