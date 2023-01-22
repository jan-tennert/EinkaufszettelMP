package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.ClipboardManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { createViewModel() }
    single { ClipboardManager() }
}