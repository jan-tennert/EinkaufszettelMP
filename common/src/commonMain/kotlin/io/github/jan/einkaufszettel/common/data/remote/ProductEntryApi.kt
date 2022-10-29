package io.github.jan.einkaufszettel.common.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.PostgrestUpdate
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class ProductEntry(
    val id: Int,
    @SerialName("created_at")
    val createdAt: Instant,
    val content: String,
    @SerialName("shop_id")
    val shopId: Int,
    @SerialName("done_by")
    val doneById: String?,
    @SerialName("user_id")
    val creatorId: String,
    @SerialName("done_since")
    val doneSince: Instant?
)

interface ProductEntryApi {

    suspend fun retrieveProducts(): List<ProductEntry>

    suspend fun createProduct(shopId: Int, content: String, creatorId: String): ProductEntry

    suspend fun markAsDone(id: Int, doneById: String)

    suspend fun markAsUndone(id: Int)

    suspend fun editContent(id: Int, content: String)

    suspend fun deleteProduct(id: Int)

}

internal class ProductEntryApiImpl(
    supabaseClient: SupabaseClient
) : ProductEntryApi {

    private val table = supabaseClient.postgrest["products"]

    override suspend fun retrieveProducts(): List<ProductEntry> = table
        .select().decodeAs()

    override suspend fun createProduct(shopId: Int, content: String, creatorId: String): ProductEntry {
        return table.insert(buildJsonObject {
            put("shop_id", shopId)
            put("content", content)
            put("user_id", creatorId)
        }).decodeSingle()
    }

    override suspend fun deleteProduct(id: Int) {
        table.delete {
            ProductEntry::id eq id
        }
    }

    override suspend fun editContent(id: Int, content: String) = updateProduct(id) {
        ProductEntry::content setTo content
    }

    override suspend fun markAsDone(id: Int, doneById: String) = updateProduct(id) {
        ProductEntry::doneById setTo doneById
    }

    override suspend fun markAsUndone(id: Int) = updateProduct(id) {
        ProductEntry::doneById setTo null
    }

    private suspend fun updateProduct(id: Int, update: PostgrestUpdate.() -> Unit) {
        table.update(
            {
                 update()
            }
        ) {
            ProductEntry::id eq id
        }
    }

}