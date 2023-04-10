package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.AppViewModel
import io.github.jan.einkaufszettel.common.data.local.ClipboardManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::AppViewModel)
    single { ClipboardManager() }
}