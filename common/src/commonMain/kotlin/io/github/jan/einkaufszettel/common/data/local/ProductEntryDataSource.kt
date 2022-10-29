package io.github.jan.einkaufszettel.common.data.local

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import einkaufszettel.db.GetAllEntries
import io.github.jan.einkaufszettel.common.data.remote.ProductEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface ProductEntryDataSource {

    suspend fun insertEntry(productEntry: ProductEntry)

    fun getAllEntries(): Flow<List<GetAllEntries>>

    suspend fun deleteEntryById(id: Long)

    suspend fun clearEntries()

    suspend fun insertAll(entries: List<ProductEntry>)

    suspend fun markEntryAsDone(id: Long, userId: String)

    suspend fun markEntryUndone(id: Long)

    suspend fun editEntryContent(id: Long, content: String)

}

internal class ProductEntryDataSourceImpl(
    db: EinkaufszettelDatabase
) : ProductEntryDataSource {

    private val queries = db.productEntryQueries

    override suspend fun deleteEntryById(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteEntryById(id)
        }
    }

    override fun getAllEntries(): Flow<List<GetAllEntries>> {
        return queries.getAllEntries().asFlow().mapToList()
    }

    override suspend fun insertEntry(productEntry: ProductEntry) {
        withContext(Dispatchers.IO) {
            queries.insertEntry(
                id = productEntry.id.toLong(),
                content = productEntry.content,
                createdAt = productEntry.createdAt,
                shopId = productEntry.shopId.toLong(),
                doneById = productEntry.doneById,
                creatorId = productEntry.creatorId
            )
        }
    }

    override suspend fun clearEntries() {
        withContext(Dispatchers.IO) {
            queries.clearEntries()
        }
    }

    override suspend fun insertAll(entries: List<ProductEntry>) {
        withContext(Dispatchers.IO) {
            val oldData = queries.getAllEntries().executeAsList()
            queries.transaction {
                val toDelete = oldData.filter { entries.none { newProduct -> newProduct.id.toLong() == it.id } }
                entries.forEach { entry ->
                    queries.insertEntry(
                        id = entry.id.toLong(),
                        content = entry.content,
                        createdAt = entry.createdAt,
                        shopId = entry.shopId.toLong(),
                        doneById = entry.doneById,
                        creatorId = entry.creatorId
                    )
                }
                toDelete.forEach {
                    queries.deleteEntryById(it.id)
                }
            }
        }
    }

    override suspend fun markEntryAsDone(id: Long, userId: String) {
        withContext(Dispatchers.IO) {
            queries.markEntryAsDone(userId, id)
        }
    }

    override suspend fun markEntryUndone(id: Long) {
        withContext(Dispatchers.IO) {
            queries.markEntryUndone(id)
        }
    }

    override suspend fun editEntryContent(id: Long, content: String) {
        withContext(Dispatchers.IO) {
            queries.editEntryContent(content, id)
        }
    }

}