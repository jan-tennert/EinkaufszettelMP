package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.ClipboardManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

actual val platformModule = module {
    viewModel { createViewModel() }
    single { ClipboardManager(androidContext()) }
}