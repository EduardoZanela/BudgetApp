package com.eduardozanela.budget.model

import java.time.LocalDate

data class TransactionRecord(
    val transactionDate: LocalDate,
    val postedDate: LocalDate,
    val description: String,
    val amount: Double
)
