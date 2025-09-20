package com.eduardozanela.budget.data

import com.eduardozanela.budget.domain.Bank
import kotlinx.datetime.LocalDate

data class TransactionRecord(
    val transactionDate: LocalDate,
    val postedDate: LocalDate,
    val description: String,
    val amount: Double,
    val category: String,
    val account: Bank
)

fun TransactionRecord.toRecord(): Record = Record(this.transactionDate.toString(), this.description, this.amount, category = this.category, account = this.account.displayName)