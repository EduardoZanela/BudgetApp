package com.eduardozanela.budget.data

import com.eduardozanela.budget.BudgetAppApplication
import com.eduardozanela.budget.Database
import com.eduardozanela.budget.data.database.DriverFactory
import com.eduardozanela.budget.domain.TransactionRepository

actual fun getTransactionRepository(): TransactionRepository {
    return LocalTransactionRepository(Database(DriverFactory(BudgetAppApplication.instance).createDriver()))
}