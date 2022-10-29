import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RootComponent : KoinComponent {

    val viewModel: EinkaufszettelViewModel by inject()

    fun destroy() {
        viewModel.onDestroy()
    }

}

@Composable
fun RootLifecycle(component: RootComponent) {
    DisposableEffect(component) {
        onDispose {
            component.destroy()
        }
    }
}