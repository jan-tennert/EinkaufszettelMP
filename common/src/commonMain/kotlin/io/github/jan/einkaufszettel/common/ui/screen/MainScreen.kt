package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.navigation.*

@Composable
expect fun BackHandle(action: () -> Unit)

@Composable
fun MainScreen(viewModel: EinkaufszettelViewModel) {
    val navController by rememberNavController(NavigationTarget.Bottom.Home)
    val currentTarget by navController.currentScreen
    println(currentTarget)
    val bottomItems = remember { NavigationTarget.Bottom.ALL }
    Scaffold(
        topBar = { TopBar(currentTarget.topTitle) },
        bottomBar = { BottomBar(currentTarget, navController, bottomItems) }
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavigationHost(navController) {
                composable(NavigationTarget.Bottom.Home::class) {
                    HomeScreen(viewModel)
                }
                composable(NavigationTarget.Bottom.Account::class) {
                    AccountScreen(viewModel)
                }
                composable(NavigationTarget.Bottom.ShoppingList::class) {
                    ShoppingListScreen(viewModel)
                }
                composable(NavigationTarget.Bottom.Cards::class) {
                    CardScreen(viewModel)
                }
                composable(NavigationTarget.Bottom.Scan::class) {
                    BarCodeScreen(viewModel)
                }
            }.build()
        }
    }
}

@Composable
fun TopBar(text: String) {
    CenterAlignedTopAppBar(
        title = { Text(text = text) }
    )
}

@Composable
fun BottomBar(
    currentTarget: NavigationTarget,
    navigationController: NavController,
    bottomItems: List<NavigationTarget.Bottom>
) {
    NavigationBar {
        bottomItems.forEach {
            val selected = currentTarget == it
            NavigationBarItem(
                selected = selected,
                onClick = { navigationController.navigate(it) },
                icon = it.icon
            )
        }
    }
}