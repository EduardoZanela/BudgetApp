package com.eduardozanela.budget.extractor

import com.eduardozanela.budget.model.TransactionRecord
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.jvm.Throws

interface BankStatementExtractor {

    @Throws(Exception::class)
    fun extract(data: String) : List<TransactionRecord>

    fun parseDate(date: String) : LocalDate {
        val inputFormatter = DateTimeFormatter.ofPattern("MMM d")
        val currentYear = LocalDate.now().year
        return LocalDate.parse("$currentYear $date", inputFormatter)
    }
}