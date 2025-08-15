package com.eduardozanela.budget.utils

import com.eduardozanela.budget.model.Record

/**
 * Parses a raw CSV string into a list of Record objects.
 * It dynamically finds column indices from the header row, making it robust.
 *
 * @param csvData The raw string content of the CSV file.
 * @return A list of parsed Record objects.
 */
fun parseCsvToRecords(csvData: String): List<Record> {
    val lines = csvData.lines().filter { it.isNotBlank() }
    if (lines.size < 2) return emptyList() // Must have at least a header and one data row

    val header = lines.first().split(',').map { it.trim().lowercase() }
    val dataRows = lines.drop(1)

    val dateIndex = header.indexOf("date")
    val amountIndex = header.indexOf("amount")
    val titleIndex = header.indexOf("title") // Use 'Title' for description
    val categoryIndex = header.indexOf("category")

    if (dateIndex == -1 || amountIndex == -1 || titleIndex == -1) {
        println("CSV Error: Header is missing required columns (Date, Amount, Title). Found: $header")
        return emptyList()
    }

    return dataRows.mapNotNull { line ->
        try {
            val columns = line.split(',')
            Record(
                date = columns.getOrNull(dateIndex)?.trim() ?: "",
                description = columns.getOrNull(titleIndex)?.trim()?.removeSurrounding("\"") ?: "",
                amount = columns.getOrNull(amountIndex)?.trim()?.toDoubleOrNull() ?: 0.0,
                category = if (categoryIndex != -1) columns.getOrNull(categoryIndex)?.trim().takeIf { !it.isNullOrBlank() } ?: "Uncategorized" else "Uncategorized"
            )
        } catch (e: Exception) {
            println("Failed to parse line: $line. Error: ${e.message}")
            null
        }
    }
}