package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.ClipboardManager
import org.koin.dsl.module

actual val platformModule = module {
    single { createViewModel() }
    single { ClipboardManager() }
}