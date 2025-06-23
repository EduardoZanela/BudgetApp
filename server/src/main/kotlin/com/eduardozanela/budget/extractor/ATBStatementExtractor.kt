package com.eduardozanela.budget.extractor

import com.eduardozanela.budget.model.TransactionRecord
import org.slf4j.LoggerFactory

class ATBStatementExtractor : BankStatementExtractor {

    private val logger = LoggerFactory.getLogger(ATBStatementExtractor::class.java)

    override fun extract(data: String): List<TransactionRecord> {

        val regex = Regex("""^(\w{3,4}\s?\d{1,2})\s+(\w{3,4}\s?\d{1,2})\s+(.+?)\s+(\d+\.\d{2})(?:\s|$)""", RegexOption.MULTILINE)
        val records = parseRecords(extractPurchasesSection(data, "PURCHASES AND RETURNS", "Total purchases"), regex)

        logger.info("ATB Statement number of records found ${records.size}")

        return records
    }
}