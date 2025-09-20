package com.eduardozanela.budget.utils

import com.eduardozanela.budget.data.Record

/**
 * Converts a list of Record objects into a CSV-formatted string.
 *
 * @param records The list of records to convert.
 * @return A string containing the data in CSV format.
 */
fun generateCsvFromRecords(records: List<Record>): String {
    val header = "Date,Name,Amount,Category,Account"
    val rows = records.map { record ->
        // To handle potential commas in the description, we wrap it in quotes.
        val description = if (record.description.contains(',')) "\"${record.description}\"" else record.description
        "${record.date},${description},${record.amount},${record.category},${record.account}"
    }
    // Combine the header and all the data rows, separated by newlines.
    return (listOf(header) + rows).joinToString("\n")
}

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

    val dateIndex = header.indexOf("Date")
    val amountIndex = header.indexOf("Amount")
    val titleIndex = header.indexOf("Title") // Use 'Title' for description
    val categoryIndex = header.indexOf("Category")
    val accountIndex = header.indexOf("Account")

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
                category = if (categoryIndex != -1) columns.getOrNull(categoryIndex)?.trim().takeIf { !it.isNullOrBlank() } ?: "Uncategorized" else "Uncategorized",
                account = columns.getOrNull(accountIndex)?.trim() ?: ""
            )
        } catch (e: Exception) {
            println("Failed to parse line: $line. Error: ${e.message}")
            null
        }
    }
}