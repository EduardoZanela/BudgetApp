package com.eduardozanela.budget.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.eduardozanela.budget.Database
import com.eduardozanela.budget.domain.Transaction
import com.eduardozanela.budget.domain.TransactionRepository
import com.eduardozanela.budget.domain.TransactionType
import com.eduardozanela.budget.sqldelight.transactions.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class LocalTransactionRepository(private val database: Database) : TransactionRepository {

    // Helper to map DB entity to domain entity
    private fun TransactionEntity.toDomain(): Transaction {
        return Transaction(
            id = id,
            amount = amount,
            description = description,
            category = category,
            type = TransactionType.valueOf(type), // Use adapter
            date = Instant.fromEpochMilliseconds(date).toLocalDateTime(TimeZone.currentSystemDefault()),
            accountId = accountId
        )
    }

    override suspend fun addTransaction(transaction: Transaction)
    {
        withContext(Dispatchers.IO) {
            database.transactionsQueries.insertTransaction(
                id = transaction.id,
                amount = transaction.amount,
                description = transaction.description ?: "",
                category = transaction.category,
                type = transaction.type.name, // Convert enum to string for DB
                date = transaction.date.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(), // Convert Instant to Long for DB
                accountId = transaction.accountId
            )
        }
    }

    override suspend fun getTransactionById(id: String): Transaction?
    {
        return withContext(Dispatchers.IO) {
            database.transactionsQueries.selectTransactionById(id)
                .asFlow()
                .mapToOneOrNull(Dispatchers.IO)
                .map { it?.toDomain() }
                .firstOrNull()
        }
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return database.transactionsQueries.selectAllTransactions()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun updateTransaction(transaction: Transaction)
    {
        withContext(Dispatchers.IO) {
            database.transactionsQueries.updateTransaction(
                amount = transaction.amount,
                description = transaction.description ?: "",
                category = transaction.category,
                type = transaction.type.name,
                date = transaction.date.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                accountId = transaction.accountId,
                id = transaction.id
            )
        }
    }

    override suspend fun deleteTransaction(id: String) {
        withContext(Dispatchers.IO) {
            database.transactionsQueries.deleteTransaction(id)
        }
    }

    override suspend fun searchTransaction(query: String, category: String?, startDate: LocalDateTime?, endDate: LocalDateTime?): Flow<List<Transaction>> {
        return withContext(Dispatchers.IO) {
            // SQLDelight takes nullable parameters for IS NULL checks
            database.transactionsQueries.searchTransactions(
                searchText = query,
                category = category,
                startDate = startDate?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds(),
                endDate = endDate?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds()
            )
                .asFlow()
                .mapToList(Dispatchers.IO)
                .map { entities -> entities.map { it.toDomain() } }
        }
    }

    override suspend fun searchTransaction(
        category: String?,
        selectedMonth: LocalDate
    ): Flow<List<Transaction>> {
        TODO("Not yet implemented")
    }
}