package com.eduardozanela.budget.presentation.transaction.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import budgetapp.composeapp.generated.resources.Res
import budgetapp.composeapp.generated.resources.transactions_screen
import com.eduardozanela.budget.di.getViewModel
import com.eduardozanela.budget.presentation.components.MonthSelector
import com.eduardozanela.budget.presentation.components.MonthSummaryIndicators
import com.eduardozanela.budget.presentation.components.TransactionItem
import com.eduardozanela.budget.presentation.transaction.TransactionListViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionListViewModel = getViewModel()
) {
    val groupedTransactions by viewModel.groupedTransactions.collectAsState()

    val selectedMonth by viewModel.selectedMonth.collectAsState()

    val totalIncome by viewModel.totalIncome.collectAsState()

    val totalExpenses by viewModel.totalExpenses.collectAsState()

    val dateFormatter = remember {
        LocalDate.Format {
            dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
            char(',')
            char(' ')
            monthName(MonthNames.ENGLISH_FULL)
            char(' ')
            dayOfMonth()
        }
    }

    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(Res.string.transactions_screen),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            textAlign = TextAlign.Center
        )

        MonthSelector(
            selectedMonth = selectedMonth,
            transactionCount = groupedTransactions.values.flatten().size,
            onMonthChange = { viewModel.onMonthChange(it) }
        )

        MonthSummaryIndicators(
            totalIncome = totalIncome,
            totalExpenses = totalExpenses
        )

        if (groupedTransactions.isEmpty()) {
            Text("No transactions found.", modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp)
            ) {
                // Get sorted keys to ensure dates are displayed in order (e.g., newest first)
                val sortedDates = groupedTransactions.keys.sortedDescending()

                sortedDates.forEach { date ->
                    stickyHeader {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth().padding(0.dp, 1.dp)
                        ) {
                            Text(
                                text = dateFormatter.format(date),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f), // occupies start side
                            )
                            Text(
                                text = "123",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f), // occupies end side
                                textAlign = TextAlign.End
                            )
                        }
                    }

                    items(
                        items = groupedTransactions[date].orEmpty(),
                        key = { transaction -> transaction.id },

                    ) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            onItemClick = {  },
                            modifier = Modifier.fillMaxWidth().padding(3.dp, 0.dp, 0.dp, 0.dp)
                        )
                    }

                    if (sortedDates.last() != date) {
                        item {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}