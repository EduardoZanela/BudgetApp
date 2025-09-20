package com.eduardozanela.budget.presentation.transaction.screens

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import budgetapp.composeapp.generated.resources.Res
import budgetapp.composeapp.generated.resources.transactions_screen
import com.eduardozanela.budget.di.getViewModel
import com.eduardozanela.budget.presentation.components.TransactionItem
import com.eduardozanela.budget.presentation.transaction.TransactionListViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionListViewModel = getViewModel()
) {
    val groupedTransactions by viewModel.groupedTransactions.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()

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
                        Text(
                            text = dateFormatter.format(date),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth().padding(0.dp, 1.dp)
                        )
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

@Composable
fun MonthSelector(
    selectedMonth: LocalDate,
    transactionCount: Int,
    onMonthChange: (LocalDate) -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(

                    onDragEnd = {
                        when {
                            offsetX > 200 -> onMonthChange(selectedMonth.minus(1, kotlinx.datetime.DateTimeUnit.MONTH))
                            offsetX < -200 -> onMonthChange(selectedMonth.plus(1, kotlinx.datetime.DateTimeUnit.MONTH))
                        }
                        offsetX = 0f
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount
                    }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onMonthChange(selectedMonth.minus(1, kotlinx.datetime.DateTimeUnit.MONTH)) }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous month")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${selectedMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedMonth.year}" ,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$transactionCount transactions",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = { onMonthChange(selectedMonth.plus(1, kotlinx.datetime.DateTimeUnit.MONTH)) }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next month")
            }
        }
    }
}
