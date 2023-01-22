package io.github.jan.einkaufszettel.common.di

import com.russhwolf.settings.ObservableSettings
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettings
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelSettingsImpl
import org.koin.dsl.module

expect fun createSettings(): ObservableSettings

val settingsModule = module {
    single {
        createSettings()
    }
    single<EinkaufszettelSettings> {
        EinkaufszettelSettingsImpl(get())
    }
}