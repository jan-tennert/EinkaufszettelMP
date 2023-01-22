package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.UpdateInstaller
import io.github.jan.einkaufszettel.common.data.remote.AutoUpdater
import io.github.jan.supabase.CurrentPlatformTarget
import io.github.jan.supabase.PlatformTarget
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.core.scope.Scope
import org.koin.dsl.module
import java.io.File

internal class AutoUpdaterImpl(
    private val httpClient: HttpClient,
    private val cacheDir: File
) : AutoUpdater {

    val repo = "jan-tennert/EinkaufszettelMP"

    override suspend fun retrieveNewestVersion(): Int {
        return httpClient.get("https://api.github.com/repos/$repo/releases/latest") {
            header("Authorization", "")
        }.body<JsonObject>()["tag_name"]?.jsonPrimitive?.content?.substring(1)?.toInt() ?: 0
    }

    override suspend fun downloadLatestVersion(version: Int, callback: (Float) -> Unit): File {
        val extension = if(CurrentPlatformTarget == PlatformTarget.ANDROID) "apk" else "exe"
        val target = File(cacheDir, "Einkaufszettel.$extension")
        if(target.exists()) target.delete()
        val response = httpClient.get("https://github.com/$repo/releases/download/v$version/Einkaufszettel.$extension") {
            onDownload { bytesSentTotal, contentLength ->
                val float = bytesSentTotal.toFloat() / contentLength.toFloat()
                callback(if(float.isNaN()) 0f else float)
            }
        }
        target.writeBytes(response.body())
        return target
    }

}

expect fun Scope.cacheDir(): File

expect fun Scope.updateInstaller(): UpdateInstaller

actual val updateModule = module {
    single { updateInstaller() }
    single<File> { cacheDir() }
    single<AutoUpdater> { AutoUpdaterImpl(get(), get()) }
}