package io.github.jan.einkaufszettel.common.di

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings

actual fun createSettings(): ObservableSettings = Settings() as ObservableSettings