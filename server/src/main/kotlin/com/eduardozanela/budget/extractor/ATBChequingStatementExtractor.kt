package com.eduardozanela.budget.extractor

import com.eduardozanela.budget.model.TransactionRecord

class ATBChequingStatementExtractor : BankStatementExtractor {

    override fun extract(data: String): List<TransactionRecord> {
        val records = mutableListOf<TransactionRecord>()

        val text = extractPurchasesSection(data, "Details of your account transactions", "Closing balance")

        return records
    }
}