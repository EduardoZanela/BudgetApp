package com.eduardozanela.budget.data

import com.eduardozanela.budget.domain.TransactionRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

private val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                println("Ktor => $message")
            }
        }
    }
}

actual fun getTransactionRepository(): TransactionRepository {
    return ApiTransactionRepository(TransactionApiService(client))
}