package io.github.jan.einkaufszettel.common.data.local

import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.LocalDateTime
import java.time.ZoneOffset

actual class DateTimeFormatter {

    actual fun format(format: String, date: Instant): String {
        val pattern = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return pattern.format(LocalDateTime.ofInstant(date.toJavaInstant(), ZoneOffset.systemDefault()))
    }

}