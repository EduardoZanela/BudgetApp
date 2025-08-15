package com.eduardozanela.budget.controller

import com.eduardozanela.budget.model.Bank
import com.eduardozanela.budget.service.StatementService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/statement")
// Allow requests from the Compose for Web development server running on localhost:8080
@CrossOrigin(origins = ["*"])
class StatementController(private val statementService: StatementService) {

    @PostMapping("/upload", produces = ["text/csv"])
    suspend fun handleUpload(@RequestPart("file") file: MultipartFile, @RequestParam("bank") bank: Bank): ResponseEntity<ByteArray> {
        val csvBytes = statementService.extract(file, bank)

        val headers = HttpHeaders().apply {
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statement.csv")
            contentType = MediaType("text", "csv")
        }

        return ResponseEntity.ok()
            .headers(headers)
            .body(csvBytes)
    }
}