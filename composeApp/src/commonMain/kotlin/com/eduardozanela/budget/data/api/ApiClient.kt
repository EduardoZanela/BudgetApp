package com.eduardozanela.budget.data.api

import com.eduardozanela.budget.data.Record
import com.eduardozanela.budget.generated.Config
import com.eduardozanela.budget.domain.Bank
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.accept
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.timeout
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*


class ApiClient {

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

    suspend fun uploadFile(fileBytes: ByteArray, fileName: String, bank: Bank): Result<List<Record>> {
        val endpoint = "${Config.API_URL}/api/statement/upload"
        return try {
            val response: HttpResponse = client.submitFormWithBinaryData(
                url = endpoint,
                formData = formData {
                    append("bank", bank.name)
                    append(
                        key = "file",
                        value = fileBytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "application/pdf")
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        }
                    )
                }
            ) {
                accept(ContentType.Application.Json)
                timeout {
                    requestTimeoutMillis = 10_000 // 10s timeout
                }
            }

            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Upload failed: ${response.status.value} ${response.bodyAsText()}"))
            }
        } catch (e: HttpRequestTimeoutException) {
            Result.failure(Exception("Request timed out after 10s", e))
        } catch (e: ClientRequestException) {
            Result.failure(Exception("Client error: ${e.response.status}", e))
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status}", e))
        } catch (e: Throwable) {
            Result.failure(Exception("Unexpected error: ${e.message}", e))
        }
    }
}