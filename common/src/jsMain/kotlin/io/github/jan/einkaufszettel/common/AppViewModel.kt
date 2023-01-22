package io.github.jan.einkaufszettel.common

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

actual open class AppViewModel actual constructor() : InstanceKeeper.Instance {
    actual val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    override fun onDestroy() {
        scope.cancel()
    }
}