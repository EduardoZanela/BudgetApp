fun buildCsv(records: List<Record>): String {
    val header = "Date,Description,Amount"
    val body = records.joinToString("\n") { "${it.date},${it.description},${it.amount}" }
    return "$header\n$body"
}

fun sampleRecords(): List<Record> = listOf(
    Record("2025-05-03", "CDN TIRE STORE", "31.42"),
    Record("2025-05-09", "PAYMENT", "-103.37")
)
