package com.eduardozanela.budget.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate as JLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object DateParser {
    private val formats = listOf(
        DateTimeFormatter.ofPattern("MM/dd/yy", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("MMM[ ]d", Locale.ENGLISH)
    )

    fun parseDate(input: String): LocalDate {
        for (formatter in formats) {
            try {
                val parsed = JLocalDate.parse(input.uppercase(), formatter)
                return parsed.toKotlinLocalDate()
            } catch (e: DateTimeParseException) {}
        }

        // Special case: when it's like "JUN 16" without year → assume current year
        try {
            val cleaned = input.replace(Regex("([A-Za-z]+)(\\d{1,2})"), "$1 $2")
            val parsed = JLocalDate.parse("$cleaned ${JLocalDate.now().year}", DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH))
            return parsed.toKotlinLocalDate()
        } catch (_: Exception) {}

        throw IllegalArgumentException("Unrecognized date format: $input")
    }
}