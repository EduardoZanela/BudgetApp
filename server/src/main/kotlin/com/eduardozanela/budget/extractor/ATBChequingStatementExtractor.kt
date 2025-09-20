package com.eduardozanela.budget.extractor

import com.eduardozanela.budget.data.TransactionRecord
import com.eduardozanela.budget.domain.Bank
import com.eduardozanela.budget.utils.DateParser
import org.slf4j.LoggerFactory

class ATBChequingStatementExtractor : BankStatementExtractor {

    companion object {

        private val receivedTransactionDesc: MutableList<String> = mutableListOf<String>()

        init {
            receivedTransactionDesc.add("INTERAC e-Transfer Received")
            receivedTransactionDesc.add("Direct Deposit")
            receivedTransactionDesc.add("Transfer From")
            receivedTransactionDesc.add("Cash deposit")
        }

        fun get(): List<String> = receivedTransactionDesc
    }

    private val logger = LoggerFactory.getLogger(ATBChequingStatementExtractor::class.java)

    override fun extract(data: String): List<TransactionRecord> {

        val text = extractPurchasesSection(data, "Details of your account transactions", "Closing balance")
        val regex = Regex("^([A-Za-z]{3} \\d{1,2}) (.+?) \\$([\\d,]+\\.\\d{2}) ([\\d,]+\\.\\d{2})$", RegexOption.MULTILINE)

        val records = parseRecords(text, Bank.ATBCHEQUING, regex) { match ->
            val (startDateStr, description, amountStr) = match.destructured
            val isDeposit = receivedTransactionDesc.any { description.contains(it) }
            var value = amountStr.replace("[^\\d.]".toRegex(), "").toDouble()
            value = if (isDeposit) value else -value

            TransactionRecord(
                DateParser.parseDate(startDateStr),
                DateParser.parseDate(startDateStr),
                description,
                value,
                category = "",
                account = Bank.ATBCHEQUING
            )
        }

        logger.info("ATB Chequing number of records found ${records.size}")

        return records
    }
}