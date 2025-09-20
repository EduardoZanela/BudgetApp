package com.eduardozanela.budget.extractor

import com.eduardozanela.budget.data.TransactionRecord
import com.eduardozanela.budget.domain.Bank
import org.slf4j.LoggerFactory

class MBNAStatementExtractor : BankStatementExtractor {

    private val logger = LoggerFactory.getLogger(MBNAStatementExtractor::class.java)

    override fun extract(data: String): List<TransactionRecord> {

        val regex = Regex("""(\d{2}/\d{2}/\d{2})\s+(\d{2}/\d{2}/\d{2})\s+(.+?)\s+\d{4}\s+\$([\d,]+\.\d{2})""")
        val records = parseRecords(data, Bank.MBNA, regex)

        logger.info("MBNA Statement number of records found ${records.size}")

        return records
    }
}