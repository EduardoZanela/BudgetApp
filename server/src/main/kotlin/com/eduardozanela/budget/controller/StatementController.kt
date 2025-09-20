package com.eduardozanela.budget.controller

import com.eduardozanela.budget.domain.Bank
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
import com.eduardozanela.budget.data.Record

@RestController
@RequestMapping("/api/statement")
@CrossOrigin(origins = ["https://budgetflow.eduardozanela.com", "http://localhost:8080"])
class StatementController(private val statementService: StatementService) {

    @PostMapping("/upload", produces = ["application/json"])
    suspend fun handleUpload(@RequestPart("file") file: MultipartFile, @RequestParam("bank") bank: Bank): ResponseEntity<List<Record>> {
        val records = statementService.extract(file, bank)

        val headers = HttpHeaders().apply {
            contentType = MediaType("application", "json")
        }

        return  ResponseEntity.ok()
            .headers(headers)
            .body(records);
    }
}