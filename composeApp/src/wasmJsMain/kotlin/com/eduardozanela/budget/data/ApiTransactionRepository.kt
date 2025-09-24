package com.eduardozanela.budget.data

import com.eduardozanela.budget.domain.Transaction
import com.eduardozanela.budget.domain.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class ApiTransactionRepository(private val apiService: TransactionApiService) : TransactionRepository {

    override suspend fun addTransaction(transaction: Transaction) {
        apiService.addTransaction(transaction)
    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return try {
            apiService.getTransaction(id)
        } catch (e: Exception) {
            null // Handle 404 or other errors
        }
    }

    override fun getAllTransactions(): Flow<List<Transaction>> = flow {
        while (true) {
            try {
                emit(apiService.getTransactions())
            } catch (e: Exception) {
                // Handle network errors, etc.
                emit(emptyList()) // Or rethrow, or log
            }
            kotlinx.coroutines.delay(5000) // Poll every 5 seconds for simplicity, SSE would be better
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        apiService.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(id: String) {
        apiService.deleteTransaction(id)
    }

    override suspend fun searchTransaction(
        query: String,
        category: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): Flow<List<Transaction>> {
        return flow {
            emit(apiService.searchTransactions(query, category, startDate, endDate))
        }
    }

    override suspend fun searchTransaction(
        category: String?,
        selectedMonth: LocalDate
    ): Flow<List<Transaction>> {
        TODO("Not yet implemented")
    }
}