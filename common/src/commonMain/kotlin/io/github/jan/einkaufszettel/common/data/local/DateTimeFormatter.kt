package io.github.jan.einkaufszettel.common.data.local

import kotlinx.datetime.Instant

expect class DateTimeFormatter() {

    fun format(format: String, date: Instant): String

}