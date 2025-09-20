package com.eduardozanela.budget.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime

interface TransactionRepository {
    suspend fun addTransaction(transaction: Transaction)
    suspend fun getTransactionById(id: String): Transaction?
    fun getAllTransactions(): Flow<List<Transaction>> // Flow for real time updates
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: String)
    @OptIn(ExperimentalTime::class)
    suspend fun searchTransaction(query: String, category: String?, startDate: LocalDateTime?, endDate: LocalDateTime?): Flow<List<Transaction>>
}