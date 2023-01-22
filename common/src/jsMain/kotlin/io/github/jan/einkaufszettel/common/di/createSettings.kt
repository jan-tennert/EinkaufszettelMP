package io.github.jan.einkaufszettel.common.di

import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.ObservableSettings

actual fun createSettings(): ObservableSettings {
    return MapSettings()
}