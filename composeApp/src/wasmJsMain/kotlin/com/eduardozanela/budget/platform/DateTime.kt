package com.eduardozanela.budget.platform

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * The Wasm/JS implementation for getting the current date as a formatted string.
 */
actual fun getCurrentDateString(): String {
    val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val year = date.year
    val month = (date.month).toString().padStart(2, '0') // getMonth() is 0-indexed
    val day = date.dayOfMonth.toString().padStart(2, '0')
    return "$year-$month-$day"
}