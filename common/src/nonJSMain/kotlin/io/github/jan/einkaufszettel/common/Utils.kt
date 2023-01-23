package io.github.jan.einkaufszettel.common

actual fun osProperty(key: String): String? = System.getProperty(key)