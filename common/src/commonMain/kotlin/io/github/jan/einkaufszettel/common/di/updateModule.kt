package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.UpdateInstaller
import io.github.jan.einkaufszettel.common.data.remote.AutoUpdater
import io.github.jan.einkaufszettel.common.data.remote.AutoUpdaterImpl
import org.koin.core.scope.Scope
import org.koin.dsl.module
import java.io.File

expect fun Scope.cacheDir(): File

expect fun Scope.updateInstaller(): UpdateInstaller

val updateModule = module {
    single { updateInstaller() }
    single<File> { cacheDir() }
    single<AutoUpdater> { AutoUpdaterImpl(get(), get()) }
}