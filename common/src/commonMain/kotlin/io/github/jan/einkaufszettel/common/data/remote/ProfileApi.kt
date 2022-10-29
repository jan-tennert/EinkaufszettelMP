package io.github.jan.einkaufszettel.common.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

@kotlinx.serialization.Serializable
data class RemoteUser(
    val id: String,
    val username: String
)

interface ProfileApi {

    suspend fun retrieveProfile(id: String): RemoteUser

    suspend fun retrieveProfilesFromIds(ids: List<String>): List<RemoteUser>

    suspend fun createProfile(id: String, username: String): RemoteUser

    suspend fun updateProfile(id: String, username: String)

}

internal class ProfileApiImpl(
    supabaseClient: SupabaseClient
) : ProfileApi {

    private val table = supabaseClient.postgrest["profiles"]

    override suspend fun createProfile(id: String, username: String): RemoteUser {
        return table.insert(RemoteUser(id, username)).decodeSingle()
    }

    override suspend fun retrieveProfile(id: String): RemoteUser {
        return table.select { RemoteUser::id eq id }.decodeSingle()
    }

    override suspend fun retrieveProfilesFromIds(ids: List<String>): List<RemoteUser> {
        return table.select { RemoteUser::id isIn ids }.decodeList()
    }

    override suspend fun updateProfile(id: String, username: String) {
        table.update({ RemoteUser::username setTo username }) { RemoteUser::id eq id }
    }

}