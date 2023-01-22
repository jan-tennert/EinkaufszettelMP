package io.github.jan.einkaufszettel.common.data.remote

interface AutoUpdater {

    suspend fun retrieveNewestVersion(): Int

    suspend fun downloadLatestVersion(version: Int, callback: (Float) -> Unit): Any

}