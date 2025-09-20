package com.eduardozanela.budget.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Theaters
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eduardozanela.budget.domain.Transaction
import com.eduardozanela.budget.domain.TransactionType

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionItem(
    transaction: Transaction,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier // Allow passing a modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(vertical = 5.dp), // Standard list item padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category Icon
        Icon(
            imageVector = getCategoryIcon(transaction.category),
            contentDescription = transaction.category, // For accessibility
            modifier = Modifier.size(40.dp) // Adjust size as needed
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Category Name and Date (if you still want to show it)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.description.takeIf { !it.isNullOrBlank() } ?: transaction.category, // Show description or category
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        // Transaction Value (Amount)
        Text(
            text = "$${(transaction.amount * 100).toInt() / 100.0}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = when (transaction.type) {
                TransactionType.INCOME -> MaterialTheme.colorScheme.primary // Or a specific green
                TransactionType.EXPENSE -> MaterialTheme.colorScheme.error // Usually red
                TransactionType.MOVE_BETWEEN_ACCOUNTS -> MaterialTheme.colorScheme.onSurface // Neutral color
            }
        )
    }
}