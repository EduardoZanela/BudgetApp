package com.eduardozanela.budget.model

import kotlinx.serialization.Serializable

@Serializable
data class Record(
    val date: String,
    val description: String,
    val amount: Double,
    var category: String
)