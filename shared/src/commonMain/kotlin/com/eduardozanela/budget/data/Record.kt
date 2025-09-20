package com.eduardozanela.budget.data

import kotlinx.serialization.Serializable

@Serializable
data class Record(
    val date: String,
    val description: String,
    val amount: Double,
    val category: String,
    val account: String
)