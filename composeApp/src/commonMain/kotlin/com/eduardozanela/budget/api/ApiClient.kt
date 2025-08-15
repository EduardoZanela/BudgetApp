package com.eduardozanela.budget.api

import com.eduardozanela.budget.model.Bank
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.accept
import io.ktor.client.plugins.logging.*
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

    suspend fun uploadFile(fileBytes: ByteArray, fileName: String, bank: Bank): Result<String> {
        // IMPORTANT: Replace with your actual API endpoint
        //Config.API_URL;
        val endpoint = "http://localhost:8081/api/statement/upload"
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
                // Explicitly tell the server we can accept a CSV response for this request.
                accept(ContentType.Text.CSV)
            }

            if (response.status.isSuccess()) {
                // On success, return the CSV content from the response body.
                Result.success(response.bodyAsText())
            } else {
                // On failure, return a more descriptive error.
                Result.failure(Exception("Upload failed with status ${response.status}: ${response.bodyAsText()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}