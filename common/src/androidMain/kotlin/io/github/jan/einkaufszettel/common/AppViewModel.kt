package io.github.jan.einkaufszettel.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.essenty.instancekeeper.InstanceKeeper

actual open class AppViewModel : ViewModel(), InstanceKeeper.Instance {

    actual val scope = viewModelScope

    override fun onDestroy() {
        //ignore
    }

}