package com.eduardozanela.budget.extractor

import com.eduardozanela.budget.data.TransactionRecord
import com.eduardozanela.budget.domain.Bank
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CTFSStatementExtractor : BankStatementExtractor {

    private val logger = LoggerFactory.getLogger(CTFSStatementExtractor::class.java)

    private fun normalizeLineBreaks(text: String): String {
        return text.replace("(?<!\\n)(?<!\\n[A-Z][a-z]{2} \\d{1,2})\\n(?![A-Z][a-z]{2} \\d{1,2})".toRegex(), " ")
    }

    private fun extractStatementDate(text: String): kotlinx.datetime.LocalDate {
        val regex = Regex("""Statement date:\s+(.*)""")
        val match = regex.find(text)

        if(match != null) {
            val (rawDate) = match.destructured // e.g., "April 27, 2025"

            // Convert to LocalDate
            val inputFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy")
            val date: LocalDate = LocalDate.parse(rawDate.trim(), inputFormat)

            return kotlinx.datetime.LocalDate.parse(date.toString())
        }

        return kotlinx.datetime.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    private fun extractInstallmentPlans(text: String): List<TransactionRecord> {
        val records = mutableListOf<TransactionRecord>()
        val date = extractStatementDate(text)

        val installmentsSectionText = extractPurchasesSection(text, "Details of your 24 Equal Payments Plan", "WAYS TO PAY")

        val regex = Regex("""([A-Z][a-z]{2} \d{2} \d{4})\s+\$([0-9.]+)\s+\$([0-9.]+)\s+\$([0-9.]+)\s+([A-Z][a-z]{2} \d{2} \d{4})""")
        val matches = regex.findAll(installmentsSectionText)

        if(matches.none()) {
            logger.warn("No Installment plans found")
        }

        matches.forEach { match ->

            val (startDate, originalAmount, currentInstallment, balanceStr, expireDate) = match.destructured

            records.add(
                TransactionRecord(
                    date,
                    date,
                    "Plan Start: $startDate Original amount: $originalAmount Balance: $balanceStr, Expiry date: $expireDate",
                    currentInstallment.toDouble(),
                    category = "",
                    account = Bank.CTFS
                )
            )
        }

        return records
    }

    override fun extract(data: String): List<TransactionRecord> {

        val regex = Regex("""([A-Z][a-z]{2} \d{2})\s+([A-Z][a-z]{2} \d{2})\s+(.+?)\s+(-?\d+\.\d{2})""")
        var text = extractPurchasesSection(data, "Purchases - Card #", "Total purchases for")
        text = normalizeLineBreaks(text)

        val records = mutableListOf<TransactionRecord>()

        records.addAll(parseRecords(text, Bank.CTFS, regex))
        records.addAll(extractInstallmentPlans(data))

        logger.info("CTFS Statement number of records found ${records.size}")

        return records
    }
}