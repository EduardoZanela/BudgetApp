package com.eduardozanela.budget.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eduardozanela.budget.domain.Transaction
import com.eduardozanela.budget.domain.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardExemple(transaction: Transaction, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (transaction.type) {
                TransactionType.INCOME -> MaterialTheme.colorScheme.secondaryContainer
                TransactionType.EXPENSE -> MaterialTheme.colorScheme.errorContainer
                TransactionType.MOVE_BETWEEN_ACCOUNTS -> MaterialTheme.colorScheme.tertiaryContainer
            }
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.description ?: "", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(transaction.category, style = MaterialTheme.typography.labelSmall)
                Text(
                    text = transaction.date.toString(),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${transaction.amount} EUR", // Adjust currency as needed
                    style = MaterialTheme.typography.titleMedium,
                    color = when (transaction.type) {
                        TransactionType.INCOME -> MaterialTheme.colorScheme.onSecondaryContainer
                        TransactionType.EXPENSE -> MaterialTheme.colorScheme.onErrorContainer
                        TransactionType.MOVE_BETWEEN_ACCOUNTS -> MaterialTheme.colorScheme.tertiaryContainer
                    }
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Close, contentDescription = "Delete Transaction")
                }
            }
        }
    }
}