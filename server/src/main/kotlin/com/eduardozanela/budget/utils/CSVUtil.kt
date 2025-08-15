package com.eduardozanela.budget.utils

import com.eduardozanela.budget.model.Bank
import com.eduardozanela.budget.model.TransactionRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.OutputStream
import java.io.PrintWriter

object CSVUtil {

    private val logger: Logger by lazy { LoggerFactory.getLogger(CSVUtil::class.java) }

    fun exportToCustomCsv(transactions: List<TransactionRecord>, out: OutputStream, bank: Bank) {
        val writer = PrintWriter(out)
        writer.println("Date,Amount,Category,Title,Note,Account")

        for(record in transactions) {
            val endDate = record.postedDate
            val formattedDate = endDate.toString()

            writer.println(
                "%s,%.2f,,\"%s\",,%s".format(
                    formattedDate,record.amount,record.description,bank.name
                )
            )
        }

        writer.flush()

        logger.info("CSV generated")
    }
}