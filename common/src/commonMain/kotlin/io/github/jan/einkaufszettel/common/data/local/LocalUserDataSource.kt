package io.github.jan.einkaufszettel.common.data.local

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import einkaufszettel.db.LocalUserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface LocalUserDataSource {

    fun getUsers(): Flow<List<LocalUserDto>>

    fun retrieveAllUsers(): List<LocalUserDto>

    suspend fun insertUser(user: io.github.jan.einkaufszettel.common.data.remote.RemoteUser)

    suspend fun insertAll(users: List<io.github.jan.einkaufszettel.common.data.remote.RemoteUser>)

    suspend fun deleteUserById(id: String)

}

internal class LocalUserDataSourceImpl(
    db: EinkaufszettelDatabase
): LocalUserDataSource {

    private val queries = db.localUserDtoQueries

    override fun getUsers(): Flow<List<LocalUserDto>> {
        return queries.getUsers().asFlow().mapToList()
    }

    override suspend fun insertAll(users: List<io.github.jan.einkaufszettel.common.data.remote.RemoteUser>) {
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

    override suspend fun insertUser(user: io.github.jan.einkaufszettel.common.data.remote.RemoteUser) {
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

    override suspend fun deleteUserById(id: String) {
        withContext(Dispatchers.IO) {
            queries.deleteUserById(id)
        }
    }

}