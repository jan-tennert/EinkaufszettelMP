@file:JvmName("PlatformSpecificModule")
package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import org.koin.core.module.Module
import org.koin.core.scope.Scope

expect val platformModule: Module

fun Scope.createViewModel() = EinkaufszettelViewModel(
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get()
)