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
data class Card(
    val id: Int,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("owner_id")
    val ownerId: String,
    @SerialName("image_path")
    val imagePath: String,
    val description: String,
    @SerialName("authorized_users")
    val authorizedUsers: List<String>?
)

interface CardApi {

    suspend fun createCard(description: String, authorizedUsers: List<String>, image: FileInfo, ownerId: String): Card

    suspend fun editCard(id: Int, description: String, authorizedUsers: List<String>): Card

    suspend fun retrieveCards() : List<Card>

    suspend fun deleteCard(id: Int, imagePath: String)

}

internal class CardApiImpl(
    supabaseClient: SupabaseClient
): CardApi {

    private val table = supabaseClient.postgrest["cards"]
    private val bucket = supabaseClient.storage["cards"]

    override suspend fun createCard(
        description: String,
        authorizedUsers: List<String>,
        image: FileInfo,
        ownerId: String
    ): Card {
        val iconPath = bucket.upload(Clock.System.now().toEpochMilliseconds().toString() + "." + image.extension, image.bytes).substringAfterLast("/")
        return table.insert(buildJsonObject {
            put("description", description)
            put("owner_id", ownerId)
            put("authorized_users", JsonArray(authorizedUsers.map { JsonPrimitive(it) }))
            put("image_path", iconPath)
        }).decodeSingle()
    }

    override suspend fun deleteCard(id: Int, imagePath: String) {
        table.delete {
            Card::id eq id
        }
        bucket.delete(imagePath)
    }

    override suspend fun editCard(id: Int, description: String, authorizedUsers: List<String>): Card {
        return table.update({
            set("authorized_users", JsonArray(authorizedUsers.map { JsonPrimitive(it) }))
            Card::description setTo description
        }) {
            Card::id eq id
        }.decodeSingle()
    }

    override suspend fun retrieveCards(): List<Card> {
        return table.select().decodeAs()
    }

}