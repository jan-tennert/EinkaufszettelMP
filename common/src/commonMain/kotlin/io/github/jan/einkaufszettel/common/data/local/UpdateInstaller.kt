package io.github.jan.einkaufszettel.common.data.local

import java.io.File

expect class UpdateInstaller {

    fun install(file: File)

}
