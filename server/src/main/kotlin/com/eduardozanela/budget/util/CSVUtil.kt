package com.eduardozanela.budget.util

import com.eduardozanela.budget.model.Bank
import com.eduardozanela.budget.model.TransactionRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CSVUtil {

    private val logger: Logger by lazy { LoggerFactory.getLogger(CSVUtil::class.java) }

    fun exportToCustomCsv(transactions: List<TransactionRecord>, filePath: String, bank: Bank) {
        FileWriter(filePath).use { writer ->
            writer.write("Date,Amount,Category,Title,Note,Account\n")

            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

            for(record in transactions) {
                val endDate = record.postedDate
                val formattedDate = endDate.atStartOfDay().format(outputFormatter)

                writer.write(
                    "%s,%.2f,,\"%s\",,%s\n".format(
                        formattedDate,record.amount,record.description,bank.name
                    )
                )
            }

            logger.info("CSV generated at $filePath")

        }
    }
}