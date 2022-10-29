package io.github.jan.einkaufszettel.common.data.local

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import einkaufszettel.db.LocalUserDto
import io.github.jan.einkaufszettel.common.data.remote.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface LocalUserDataSource {

    fun getUsers(): Flow<List<LocalUserDto>>

    fun retrieveAllUsers(): List<LocalUserDto>

    suspend fun insertUser(user: UserProfile)

    suspend fun insertAll(users: List<UserProfile>)

}

internal class LocalUserDataSourceImpl(
    db: EinkaufszettelDatabase
): LocalUserDataSource {

    private val queries = db.localUserDtoQueries

    override fun getUsers(): Flow<List<LocalUserDto>> {
        return queries.getUsers().asFlow().mapToList()
    }

    override suspend fun insertAll(users: List<UserProfile>) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                users.forEach { user ->
                    queries.insertUser(
                        id = user.id,
                        username = user.username
                    )
                }
            }
        }
    }

    override suspend fun insertUser(user: UserProfile) {
        withContext(Dispatchers.IO) {
            queries.insertUser(
                id = user.id,
                username = user.username
            )
        }
    }

    override fun retrieveAllUsers(): List<LocalUserDto> {
        return queries.getUsers().executeAsList()
    }

}