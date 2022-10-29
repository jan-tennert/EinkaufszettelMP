package io.github.jan.einkaufszettel.common.data.local

import java.awt.Desktop
import java.io.File

actual class UpdateInstaller {

    actual fun install(file: File) {
        Desktop.getDesktop().open(file)
    }

}