package io.github.jan.einkaufszettel.common.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.jan.einkaufszettel.common.ui.icons.CreditCard
import io.github.jan.einkaufszettel.common.ui.icons.Home
import io.github.jan.einkaufszettel.common.ui.icons.ListAlt
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import io.github.jan.einkaufszettel.common.ui.icons.MaterialIconDimension
import io.github.jan.einkaufszettel.common.ui.icons.Restaurant
import io.github.jan.einkaufszettel.common.ui.icons.Settings
import io.github.jan.einkaufszettel.common.ui.icons.resourceIcon
import io.github.jan.supabase.CurrentPlatformTarget
import io.github.jan.supabase.PlatformTarget

sealed interface NavigationTarget {

    val topTitle: String
    val route: String

    sealed class Bottom(override val topTitle: String, override val route: String, val icon: @Composable () -> Unit):
        NavigationTarget {

        constructor(topTitle: String, route: String, icon: ImageVector) : this(topTitle, route, { Icon(icon, topTitle) })
        constructor(topTitle: String, route: String, icon: String) : this(topTitle, route, { Icon(resourceIcon(icon), topTitle, modifier = Modifier.size(MaterialIconDimension.dp)) })

        object Home: Bottom("Startseite", "home", LocalIcon.Home)
        object ShoppingList: Bottom("Einkaufszettel", "shopping_list", LocalIcon.ListAlt)
        object Recipes: Bottom("Rezepte", "recipes", LocalIcon.Restaurant)
        object Cards: Bottom("Karten", "cards", LocalIcon.CreditCard)
        object Scan: Bottom("Barcode Scanner", "scan","barcode_scanner.xml")
      //  object Account: Bottom("Account", "account", LocalIcon.AccountCircle)
        object Settings: Bottom("Einstellungen", "settings", LocalIcon.Settings)

        companion object {
            val ALL get() = buildList {
                add(Home)
                add(ShoppingList)
                add(Recipes)
                add(Cards)
                if(CurrentPlatformTarget == PlatformTarget.ANDROID) add(Scan)
          //      add(Account)
                add(Settings)
            }
        }

    }

}