// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.jan.einkaufszettel.common.di.initKoin
import io.github.jan.einkaufszettel.common.ui.App

fun main() {
    initKoin()

    val rootComponent = RootComponent()

    application {

        RootLifecycle(rootComponent)

        Window(onCloseRequest = ::exitApplication, title = "Einkaufszettel") {
            App(rootComponent.viewModel)
        }
    }
}
