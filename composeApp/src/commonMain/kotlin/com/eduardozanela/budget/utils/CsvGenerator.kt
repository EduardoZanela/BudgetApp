package com.eduardozanela.budget.utils

import com.eduardozanela.budget.model.Record

/**
 * Converts a list of Record objects into a CSV-formatted string.
 *
 * @param records The list of records to convert.
 * @return A string containing the data in CSV format.
 */
fun generateCsvFromRecords(records: List<Record>): String {
    val header = "Date,Description,Amount,Category"
    val rows = records.map { record ->
        // To handle potential commas in the description, we wrap it in quotes.
        val description = if (record.description.contains(',')) "\"${record.description}\"" else record.description
        "${record.date},${description},${record.amount},${record.category}"
    }
    // Combine the header and all the data rows, separated by newlines.
    return (listOf(header) + rows).joinToString("\n")
}