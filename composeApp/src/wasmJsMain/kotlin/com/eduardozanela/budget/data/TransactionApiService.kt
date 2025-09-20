package com.eduardozanela.budget.data

import com.eduardozanela.budget.domain.Transaction
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.LocalDateTime

class TransactionApiService(private val httpClient: HttpClient) {
    private val BASE_URL = "http://localhost:8080/api/transactions" // Your backend API base URL

    suspend fun getTransactions(): List<Transaction> {
        return httpClient.get(BASE_URL).body()
    }

    suspend fun getTransaction(id: String): Transaction {
        return httpClient.get("$BASE_URL/$id").body()
    }

    suspend fun addTransaction(transaction: Transaction) {
        httpClient.post(BASE_URL) {
            contentType(ContentType.Application.Json)
            setBody(transaction)
        }
    }

    suspend fun updateTransaction(transaction: Transaction) {
        httpClient.put("$BASE_URL/${transaction.id}") {
            contentType(ContentType.Application.Json)
            setBody(transaction)
        }
    }

    suspend fun deleteTransaction(id: String) {
        httpClient.delete("$BASE_URL/$id")
    }

    suspend fun searchTransactions(
        query: String,
        category: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): List<Transaction> {
        return httpClient.get(BASE_URL) {
            url {
                parameters.append("query", query)
                category?.let { parameters.append("category", it) }
                startDate?.let { parameters.append("startDate", it.toString()) }
                endDate?.let { parameters.append("endDate", it.toString()) }
            }
        }.body()
    }
}