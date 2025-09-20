package com.eduardozanela.budget.extractor

import com.eduardozanela.budget.data.TransactionRecord
import com.eduardozanela.budget.domain.Bank
import org.slf4j.LoggerFactory

class NeoStatementExtractor : BankStatementExtractor {

    private val logger = LoggerFactory.getLogger(NeoStatementExtractor::class.java)

    override fun extract(data: String): List<TransactionRecord> {

        val regex = Regex("""([A-Z][a-z]{2} \d{1,2})\s+([A-Z][a-z]{2} \d{1,2})\s+(.+?)\s+([-+]?\d+\.\d{2})""")
        val records = parseRecords(data, Bank.NEO, regex)

        logger.info("NEO Statement number of records found ${records.size}")

        return records
    }
}