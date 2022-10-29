package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import io.github.jan.einkaufszettel.common.data.remote.FileInfo
import java.awt.FileDialog
import java.awt.Frame

@Composable
actual fun ImageChooser(show: Boolean, onSelect: (info: FileInfo) -> Unit) {
    if(show) {
        AwtWindow(
            create = {
                object : FileDialog((null as Frame?), "Bild auswählen", LOAD) {
                    override fun setVisible(value: Boolean) {
                        super.setVisible(value)
                        if (value) {
                            val files = files.toList()
                            if(files.isNotEmpty()) {
                                val file = files.first()
                                onSelect(FileInfo(file.extension, file.readBytes()))
                            }
                        }
                    }
                }
            },
            dispose = FileDialog::dispose
        )
    }
}