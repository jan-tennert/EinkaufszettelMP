package io.github.jan.einkaufszettel.common.data.local

import java.awt.Desktop
import java.io.File
import kotlin.system.exitProcess

actual class UpdateInstaller {

    actual fun install(file: File) {
        Desktop.getDesktop().open(file)
        exitProcess(0)
    }

}