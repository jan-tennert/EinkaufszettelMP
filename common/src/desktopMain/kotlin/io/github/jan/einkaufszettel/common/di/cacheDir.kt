package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.UpdateInstaller
import org.koin.core.scope.Scope
import java.io.File

actual fun Scope.cacheDir(): File {
    return File(System.getProperty("java.io.tmpdir"))
}

actual fun Scope.updateInstaller() = UpdateInstaller()