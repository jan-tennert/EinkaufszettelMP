
import androidx.compose.material3.*
import androidx.compose.ui.window.Window
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelDatabase
import io.github.jan.einkaufszettel.common.di.initKoin
import io.github.jan.einkaufszettel.common.ui.App
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

class RootComponent : KoinComponent {

    val viewModel: EinkaufszettelViewModel by inject()

}

@OptIn(ExperimentalMaterial3Api::class)
fun main() {
    Settings()
    initSqlDriver(EinkaufszettelDatabase.Schema).then { driver ->
        onWasmReady {
            initKoin {
                modules(module {
                    single { driver }
                })
            }
            val rootComponent = RootComponent()
            Window(title = "Einkaufszettel") {
                App(rootComponent.viewModel)
            }
        }
    }
}

