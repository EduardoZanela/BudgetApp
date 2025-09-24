package com.eduardozanela.budget.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eduardozanela.budget.di.getViewModel
import com.eduardozanela.budget.presentation.transaction.SearchTransactionViewModel
import com.eduardozanela.budget.presentation.transaction.TransactionListViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFilters(
    modifier: Modifier = Modifier,
    viewModel: SearchTransactionViewModel = getViewModel()
) {
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val showStartDatePicker = remember { mutableStateOf(false) }
    val showEndDatePicker = remember { mutableStateOf(false) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val availableCategories by viewModel.availableCategories.collectAsState(initial = emptyList())

    // Search Bar
    OutlinedTextField(
        value = searchQuery,
        onValueChange = viewModel::onSearchQueryChange,
        label = { Text("Search description or category") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Clear Search")
                }
            }
        },
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Filters Section
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category Filter (Dropdown)
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = selectedCategory ?: "All Categories",
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("All Categories") },
                    onClick = {
                        viewModel.onCategorySelected(null)
                        expanded = false
                    }
                )
                availableCategories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            viewModel.onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Date Range Filters
        IconButton(onClick = { showStartDatePicker.value = true }) {
            Icon(Icons.Filled.DateRange, contentDescription = "Select Start Date")
        }
        Text(
            text = startDate?.toString() ?: "Start Date",
            modifier = Modifier.clickable { showStartDatePicker.value = true }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("-")
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = { showEndDatePicker.value = true }) {
            Icon(Icons.Filled.DateRange, contentDescription = "Select End Date")
        }
        Text(
            text = endDate?.toString() ?: "End Date",
            modifier = Modifier.clickable { showEndDatePicker.value = true }
        )
    }
    Button(onClick = viewModel::clearFilters, modifier = Modifier.fillMaxWidth()) {
        Text("Clear Filters")
    }


    if (showStartDatePicker.value) {
        DatePickerDialogWrapper(
            onDismissRequest = { showStartDatePicker.value = false },
            onDateSelected = { selectedDate ->
                viewModel.onStartDateSelected(selectedDate)
                showStartDatePicker.value = false
            },
            initialDate = startDate ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    if (showEndDatePicker.value) {
        DatePickerDialogWrapper(
            onDismissRequest = { showEndDatePicker.value = false },
            onDateSelected = { selectedDate ->
                viewModel.onEndDateSelected(selectedDate)
                showEndDatePicker.value = false
            },
            initialDate = endDate ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }
}