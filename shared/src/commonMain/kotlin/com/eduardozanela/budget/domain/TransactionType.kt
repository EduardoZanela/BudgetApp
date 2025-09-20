package com.eduardozanela.budget.domain

enum class TransactionType(val displayName: String) {
    INCOME(displayName = "Income"),
    EXPENSE(displayName = "Expense"),
    MOVE_BETWEEN_ACCOUNTS(displayName = "Transfer")
}