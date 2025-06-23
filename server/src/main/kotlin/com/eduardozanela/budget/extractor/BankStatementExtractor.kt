package com.eduardozanela.budget.extractor

import com.eduardozanela.budget.model.TransactionRecord
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.jvm.Throws

interface BankStatementExtractor {

    @Throws(Exception::class)
    fun extract(data: String) : List<TransactionRecord>

    fun parseDate(date: String) : LocalDate {
        val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

        // e.g., input = "Jun 16"
        val parts = mutableListOf<String>()
        parts.addAll(date.split(" "))
        if(parts.size < 2) {
            parts.clear()
            parts.add(date.substring(0, 3))
            parts.add(date.substring(3))
        }
        val monthStr = parts[0].lowercase()
        val day = parts[1].toInt()

        val month = when (monthStr) {
            "jan" -> 1; "feb" -> 2; "mar" -> 3; "apr" -> 4
            "may" -> 5; "jun" -> 6; "jul" -> 7; "aug" -> 8
            "sep" -> 9; "oct" -> 10; "nov" -> 11; "dec" -> 12
            else -> error("Invalid month: $monthStr")
        }

        return LocalDate(currentYear, month, day)
    }

    fun parseRecords(data: String, regex: Regex) : List<TransactionRecord> {

        val records = mutableListOf<TransactionRecord>()
        val matches = regex.findAll(data)

        matches.forEach { match ->
            val (startDateStr, postedDateStr, description, amountStr) = match.destructured
            records.add(
                TransactionRecord(
                    parseDate(startDateStr),
                    parseDate(postedDateStr),
                    description,
                    amountStr.toDouble()
                )
            )
        }
        return records
    }

    fun extractPurchasesSection(rawText: String, start: String, end: String): String {
        val lines = rawText.split("\\R".toRegex())
        var inPurchases = false
        val section = StringBuilder()

        for(line in lines) {
            if(line.contains(start)) {
                inPurchases = true
            }
            else if(inPurchases && line.trim().startsWith(end)) {
                inPurchases = false
            }
            else if(inPurchases && !line.trim().isEmpty()) {
                section.append(line.trim()).append("\n")
            }
        }

        return section.toString()
    }
}