package io.github.jan.einkaufszettel.common.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

/**
 * NavigationHost class
 */
class NavigationHost(
    val navController: NavController,
    val contents: @Composable NavigationGraphBuilder.() -> Unit
) {

    @Composable
    fun build() {
        NavigationGraphBuilder().renderContents()
    }

    inner class NavigationGraphBuilder(
        val navController: NavController = this@NavigationHost.navController
    ) {
        @Composable
        fun renderContents() {
            this@NavigationHost.contents(this)
        }
    }
}


/**
 * Composable to build the Navigation Host
 */
@Composable
fun <T : NavigationTarget> NavigationHost.NavigationGraphBuilder.composable(
    target: KClass<T>,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = target.isInstance(navController.currentScreen.value),
        enter = fadeIn() + expandHorizontally(),
        exit = fadeOut() + shrinkHorizontally()
    ) {
        content()
    }
}