package io.github.jan.einkaufszettel.common.data.local

import kotlinx.datetime.Instant
import kotlinx.datetime.toJSDate

actual class DateTimeFormatter actual constructor() {

    actual fun format(format: String, date: Instant): String {
        return date.toJSDate().toDateString()
    }

}