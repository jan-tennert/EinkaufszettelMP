package io.github.jan.einkaufszettel.common.data.local

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import einkaufszettel.db.ShopDto
import io.github.jan.einkaufszettel.common.data.remote.Shop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

object ShopDtpSerializer: KSerializer<ShopDto> {

    override val descriptor = buildClassSerialDescriptor("ShopDto") {
        element("id", Int.serializer().descriptor)
        element("name", String.serializer().descriptor)
        element("iconUrl", String.serializer().descriptor)
        element("ownerId", String.serializer().descriptor)
        element("authorizedUsers", ListSerializer(String.serializer()).descriptor)
        element("createdAt", Instant.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): ShopDto {
        decoder as JsonDecoder
        val input = decoder.decodeJsonElement().jsonObject
        val id = input["id"]!!.jsonPrimitive.long
        val name = input["name"]!!.jsonPrimitive.content
        val iconUrl = input["iconUrl"]!!.jsonPrimitive.content
        val ownerId = input["ownerId"]!!.jsonPrimitive.content
        val authorizedUsers = try {
            Json.decodeFromJsonElement<List<String>>(input["authorizedUsers"]!!.jsonArray)
        } catch(e: Exception) {
            listOf()
        }
        val createdAt = Instant.fromEpochMilliseconds(input["createdAt"]!!.jsonPrimitive.long)
        return ShopDto(
            id = id,
            name = name,
            iconUrl = iconUrl,
            ownerId = ownerId,
            authorizedUsers = authorizedUsers,
            createdAt = createdAt,
            isVisible = true,
            isPinned = false
        )
    }

    override fun serialize(encoder: Encoder, value: ShopDto) {
        encoder as JsonEncoder
        encoder.encodeJsonElement(buildJsonObject {
            put("id", JsonPrimitive(value.id))
            put("name", JsonPrimitive(value.name))
            put("iconUrl", JsonPrimitive(value.iconUrl))
            put("ownerId", JsonPrimitive(value.ownerId))
            put("authorizedUsers", Json.encodeToJsonElement(value.authorizedUsers))
            put("createdAt", JsonPrimitive(value.createdAt.toEpochMilliseconds()))
        })
    }

}

interface ShopDataSource {

    suspend fun insertShop(shop: Shop)

    fun getAllShops(): Flow<List<ShopDto>>

    suspend fun deleteById(id: Long)

    suspend fun clearShops()

    suspend fun insertAll(shops: List<Shop>)

    suspend fun changeShopVisibility(id: Long, isVisible: Boolean)

    suspend fun changeShopPinned(id: Long, isPinned: Boolean)

}

internal class ShopDataSourceImpl(
    private val db: EinkaufszettelDatabase
) : ShopDataSource {

    private val queries = db.shopDtoQueries

    override suspend fun deleteById(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteShopById(id)
        }
    }

    override fun getAllShops(): Flow<List<ShopDto>> {
        return queries.getAllShops().asFlow().mapToList()
    }

    override suspend fun insertShop(shop: Shop) {
        withContext(Dispatchers.IO) {
            queries.insertShop(
                id = shop.id.toLong(),
                name = shop.name,
                createdAt = shop.createdAt,
                iconUrl = shop.iconUrl,
                ownerId = shop.ownerId,
                authorizedUsers = shop.authorizedUsers,
                isVisible = true,
                isPinned = false
            )
        }
    }

    override suspend fun clearShops() {
        withContext(Dispatchers.IO) {
            queries.clearShops()
        }
    }

    override suspend fun insertAll(shops: List<Shop>) {
        withContext(Dispatchers.IO) {
            val oldData = queries.getAllShops().executeAsList()
            queries.transaction {
                val toDelete = oldData.filter { shops.none { newShop -> newShop.id.toLong() == it.id } }
                shops.forEach {
                    val oldShop = oldData.firstOrNull { oldShop -> oldShop.id == it.id.toLong() }
                    queries.insertShop(
                        id = it.id.toLong(),
                        name = it.name,
                        createdAt = it.createdAt,
                        iconUrl = it.iconUrl,
                        ownerId = it.ownerId,
                        authorizedUsers = it.authorizedUsers,
                        isVisible = oldShop?.isVisible ?: true,
                        isPinned = oldShop?.isPinned ?: false
                    )
                }
                toDelete.forEach {
                    queries.deleteShopById(it.id)
                }
            }
        }
    }

    override suspend fun changeShopVisibility(id: Long, isVisible: Boolean) {
        withContext(Dispatchers.IO) {
            queries.changeShopVisibility(isVisible, id)
        }
    }

    override suspend fun changeShopPinned(id: Long, isPinned: Boolean) {
        withContext(Dispatchers.IO) {
            queries.changeShopPinned(isPinned, id)
        }
    }

}