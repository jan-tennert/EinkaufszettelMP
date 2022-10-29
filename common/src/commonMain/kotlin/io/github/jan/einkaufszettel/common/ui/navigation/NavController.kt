package io.github.jan.einkaufszettel.common.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class NavController(
    private val startDestination: NavigationTarget,
    private var backStackScreen: MutableSet<NavigationTarget> = mutableSetOf()
) {

    var currentScreen: MutableState<NavigationTarget> = mutableStateOf(startDestination)

    fun navigate(route: NavigationTarget) {
        if(route != currentScreen.value) {
            if(backStackScreen.contains(currentScreen.value) && currentScreen.value != startDestination) {
                backStackScreen.remove(currentScreen.value)
            }

            if(route == startDestination) {
                backStackScreen = mutableSetOf()
            } else {
                backStackScreen.add(currentScreen.value)
            }

            currentScreen.value = route
        }
    }

    fun navigateBack() {
        if(backStackScreen.isNotEmpty()) {
            currentScreen.value = backStackScreen.last()
            backStackScreen.remove(currentScreen.value)
        }
    }

}

@Composable
fun rememberNavController(startDestination: NavigationTarget) = remember {
    mutableStateOf(NavController(startDestination))
}