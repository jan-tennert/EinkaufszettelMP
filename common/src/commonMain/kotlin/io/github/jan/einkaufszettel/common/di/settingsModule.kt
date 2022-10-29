package io.github.jan.einkaufszettel.common.di

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettingsImpl
import org.koin.dsl.module

val settingsModule = module {
    single {
        Settings() as ObservableSettings
    }
    single<EinkaufszettelSettings> {
        EinkaufszettelSettingsImpl(get())
    }
}