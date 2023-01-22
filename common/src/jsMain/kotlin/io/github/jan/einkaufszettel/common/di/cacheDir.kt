package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.UpdateInstaller
import io.github.jan.einkaufszettel.common.data.remote.AutoUpdater
import org.koin.dsl.module

internal class AutoUpdaterDummy : AutoUpdater {

    override suspend fun retrieveNewestVersion(): Int {
        return 0
    }

    override suspend fun downloadLatestVersion(version: Int, callback: (Float) -> Unit): Any {
        return Any()
    }

}

actual val updateModule = module {
    single<AutoUpdater> { AutoUpdaterDummy() }
    single { UpdateInstaller() }
}