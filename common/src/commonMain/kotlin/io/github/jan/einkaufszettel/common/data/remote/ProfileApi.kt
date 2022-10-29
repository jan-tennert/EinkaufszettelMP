package io.github.jan.einkaufszettel.common.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

@kotlinx.serialization.Serializable
data class UserProfile(
    val id: String,
    val username: String
)

interface ProfileApi {

    suspend fun retrieveProfile(id: String): UserProfile

    suspend fun retrieveProfilesFromIds(ids: List<String>): List<UserProfile>

    suspend fun createProfile(id: String, username: String): UserProfile

    suspend fun updateProfile(id: String, username: String)

}

internal class ProfileApiImpl(
    supabaseClient: SupabaseClient
) : ProfileApi {

    private val table = supabaseClient.postgrest["profiles"]

    override suspend fun createProfile(id: String, username: String): UserProfile {
        return table.insert(UserProfile(id, username)).decodeSingle()
    }

    override suspend fun retrieveProfile(id: String): UserProfile {
        return table.select { UserProfile::id eq id }.decodeSingle()
    }

    override suspend fun retrieveProfilesFromIds(ids: List<String>): List<UserProfile> {
        return table.select { UserProfile::id isIn ids }.decodeList()
    }

    override suspend fun updateProfile(id: String, username: String) {
        table.update({ UserProfile::username setTo username }) { UserProfile::id eq id }
    }

}