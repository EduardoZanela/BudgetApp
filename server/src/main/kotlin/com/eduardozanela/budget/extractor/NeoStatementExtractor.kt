package com.eduardozanela.budget.extractor

import com.eduardozanela.budget.model.TransactionRecord
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NeoStatementExtractor : BankStatementExtractor {

    private val logger = LoggerFactory.getLogger(NeoStatementExtractor::class.java)

    override fun extract(data: String): List<TransactionRecord> {

        val records = mutableListOf<TransactionRecord>()

        val regex = """([A-Z][a-z]{2} \d{1,2})\s+([A-Z][a-z]{2} \d{1,2})\s+(.+?)\s+([-+]?\d+\.\d{2})""".toRegex()
        val matches = regex.findAll(data)

        if(matches.none()) {
            logger.warn("No records found for the statement")
        }

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



}