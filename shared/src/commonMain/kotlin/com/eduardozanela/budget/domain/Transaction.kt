package com.eduardozanela.budget.domain

import kotlinx.datetime.LocalDateTime

data class Transaction(
    val id: String,
    val amount: Double,
    val description: String?,
    val category: String,
    val type: TransactionType,
    val date: LocalDateTime,
    val accountId: String
)