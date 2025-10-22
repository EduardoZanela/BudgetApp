package com.eduardozanela.budget.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Theaters
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

private const val DECIMAL_REPLACER = "00"

fun amountFormaterWithDollar(amount: Double): String {
    val amountString = ((amount * 100).toInt() / 100.00).toString()
    val integerPart = amountString.substringBefore('.')
    var decimalPart = if (amountString.contains('.')) amountString.substringAfter('.') else DECIMAL_REPLACER
    if (decimalPart.length == 1) decimalPart += "0"

    val formattedIntegerPart = integerPart
        .reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()

    return "$$formattedIntegerPart.$decimalPart"
}

// Placeholder function - You'll need to expand this with your actual categories and icons
@Composable
fun getCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) { // Case-insensitive matching is good practice
        "salary" -> Icons.Outlined.AccountBalanceWallet
        "groceries" -> Icons.Outlined.ShoppingCart
        "transport" -> Icons.Outlined.DirectionsCar
        "rent" -> Icons.Outlined.Home
        "utilities" -> Icons.Outlined.Lightbulb
        "entertainment" -> Icons.Outlined.Theaters
        "food" -> Icons.Outlined.Fastfood
        // Add more categories and their corresponding icons
        else -> Icons.AutoMirrored.Outlined.Label // Default icon
    }
}