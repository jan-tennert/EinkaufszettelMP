package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.UpdateInstaller
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import java.io.File

actual fun Scope.cacheDir(): File {
    return androidContext().cacheDir
}

actual fun Scope.updateInstaller() = UpdateInstaller(androidContext())