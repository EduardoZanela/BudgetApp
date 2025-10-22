package com.eduardozanela.budget.presentation.transaction.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.eduardozanela.budget.domain.TransactionType
import com.eduardozanela.budget.presentation.components.DatePickerDialogWrapper
import com.eduardozanela.budget.presentation.transaction.AddTransactionViewModel
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import budgetapp.composeapp.generated.resources.Res
import budgetapp.composeapp.generated.resources.add_transaction_screen
import com.eduardozanela.budget.di.getViewModel
import com.eduardozanela.budget.domain.Bank
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Title
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import com.eduardozanela.budget.presentation.transaction.AddTransactionErrorType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.stringResource

@Composable
private fun thousandSeparatorTransformation(): VisualTransformation {
    return VisualTransformation { text ->
        val originalText = text.text.replace(",", "")
        if (originalText.isBlank() || originalText == ".")
            // Labeled return
            return@VisualTransformation TransformedText(
                AnnotatedString(originalText),
                OffsetMapping.Identity
            )

        val integerPart = originalText.substringBefore('.')
        val decimalPart = if (originalText.contains('.')) "." + originalText.substringAfter('.') else ""

        val formattedIntegerPart = integerPart
            .reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()

        val formattedText = formattedIntegerPart + decimalPart

        val offsetMapping = ThousandSeparatorOffsetMapping(originalText, formattedText)
        TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    modifier: Modifier = Modifier,
    viewModel: AddTransactionViewModel = getViewModel(),
) {
    val amount by viewModel.amount.collectAsState()
    val description by viewModel.description.collectAsState()
    val category by viewModel.category.collectAsState()
    val type by viewModel.type.collectAsState()
    val date by viewModel.date.collectAsState()
    var account by remember { mutableStateOf(Bank.ATB) }
    val message by viewModel.message.collectAsState()
    var categories by remember { mutableStateOf(listOf("Salary", "Groceries", "Transport", "Rent", "Utilities", "Entertainment", "Food")) }
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val dateFormatter = remember {
        LocalDateTime.Format {
            dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
            char(',')
            char(' ')
            monthName(MonthNames.ENGLISH_FULL)
            char(' ')
            dayOfMonth()
            char(',')
            char(' ')
            year()
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            stringResource(Res.string.add_transaction_screen),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.width(IntrinsicSize.Min).padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(end = 4.dp),
                fontWeight = FontWeight.Bold
            )
            BasicTextField(
                value = amount,
                onValueChange = { newAmount ->
                    val filteredAmount = newAmount.filter { it.isDigit() || it == '.' }
                    val parts = filteredAmount.split('.')
                    if (parts.size <= 2 && (parts.size == 1 || parts[1].length <= 2)) {
                        viewModel.onAmountChange(filteredAmount)
                    }                },
                textStyle = TextStyle(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .widthIn(min = 1.dp) // Set a minimum width to ensure cursor is visible
                    .focusRequester(focusRequester),
                visualTransformation = thousandSeparatorTransformation(),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Align content to the start
                    ) {
                        innerTextField()
                    }
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for(tType in TransactionType.entries) {
                FilterChip(
                    selected = type == tType,
                    onClick = { viewModel.onTypeChange(tType) },
                    label = { Text(tType.displayName) },
                    modifier = Modifier.padding(5.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (type == tType) MaterialTheme.colorScheme.primary else Color.Transparent,
                        selectedBorderColor = MaterialTheme.colorScheme.primary,
                        selectedBorderWidth = 1.dp,
                        enabled = true,
                        selected = type == tType
                    )
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "Select Date",
                    modifier = Modifier.size(36.dp)
                )
            }
            Text(
                text = dateFormatter.format(date),
                modifier = Modifier.fillMaxWidth().weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        OutlinedTextField(
            value = description,
            onValueChange = viewModel::onDescriptionChange,
            placeholder = { Text("Title") },
            modifier = Modifier.fillMaxWidth().padding(top = 25.dp),
            shape = RoundedCornerShape(15.dp),
            leadingIcon = {
                Icon(
                    Icons.Filled.Title,
                    contentDescription = "Description Icon"
                )
            },
            isError = message?.type == AddTransactionErrorType.EMPTY_DESCRIPTION,
            supportingText = {
                if (message?.type == AddTransactionErrorType.EMPTY_DESCRIPTION) {
                    Text(message?.message ?: "")
                }
            },
            singleLine = true
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth().padding(0.dp)
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {
                    expanded = true
                    viewModel.onCategoryChange(it)
                },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryEditable)
                    .onFocusChanged { focusState -> expanded = focusState.isFocused },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Category,
                        contentDescription = "Description Icon"
                    )
                },
                isError = message?.type == AddTransactionErrorType.EMPTY_CATEGORY,
                supportingText = {
                    if(message?.type == AddTransactionErrorType.EMPTY_CATEGORY) {
                        Text(message?.message ?: "")
                    }
                },
                shape = RoundedCornerShape(15.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.filter { it.contains(category, ignoreCase = true) }.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            viewModel.onCategoryChange(selectionOption)
                            expanded = false
                        },
                    )
                }
                if (category.isNotEmpty() && !categories.any { it.equals(category, ignoreCase = true) }) {
                    DropdownMenuItem(
                        text = { Text("Add \"$category\"") },
                        onClick = { categories = categories + category; expanded = false },
                    )
                }
            }
        }

        OutlinedTextField(
            value = description,
            onValueChange = viewModel::onDescriptionChange,
            placeholder = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            leadingIcon = {
                Icon(
                    Icons.AutoMirrored.Filled.Notes,
                    contentDescription = "Description Icon"
                )
            },
            isError = message?.type == AddTransactionErrorType.EMPTY_DESCRIPTION,
            supportingText = {
                if (message?.type == AddTransactionErrorType.EMPTY_DESCRIPTION) {
                    Text(message?.message ?: "")
                }
            },
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = viewModel::onDescriptionChange,
            placeholder = { Text("Recurrence") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            leadingIcon = {
                Icon(
                    Icons.Filled.Repeat,
                    contentDescription = "Description Icon"
                )
            },
            isError = message?.type == AddTransactionErrorType.EMPTY_DESCRIPTION,
            supportingText = {
                if (message?.type == AddTransactionErrorType.EMPTY_DESCRIPTION) {
                    Text(message?.message ?: "")
                }
            },
            singleLine = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (bank in Bank.entries) {
                FilterChip(
                    selected = account == bank,
                    onClick = { account = bank },
                    label = { Text(bank.displayName) },
                    modifier = Modifier.padding(5.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (bank == account) MaterialTheme.colorScheme.primary else Color.Transparent,
                        selectedBorderColor = MaterialTheme.colorScheme.primary,
                        selectedBorderWidth = 1.dp,
                        enabled = true,
                        selected = bank == account
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

        Button(
            onClick = viewModel::saveTransaction,
            modifier = Modifier.fillMaxWidth().padding(30.dp, 0.dp)
        ) {
            Text("Save Transaction")
        }
    }

    if (showDatePicker) {
        DatePickerDialogWrapper(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { selectedDate ->
                viewModel.onDateChange(selectedDate)
                showDatePicker = false
            },
            initialDate = date
        )
    }
}

private class ThousandSeparatorOffsetMapping(
    private val originalText: String,
    private val formattedText: String
) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        if (originalText.isEmpty()) return 0
        val originalIntegerPart = originalText.substringBefore('.')
        if (offset >= originalIntegerPart.length) {
            return formattedText.length - (originalText.length - offset)
        }
        val commasBefore = (formattedText.substringBefore('.').length - originalIntegerPart.length)
        return offset + commasBefore
    }

    override fun transformedToOriginal(offset: Int): Int {
        val commas = formattedText.take(offset).count { it == ',' }
        return (offset - commas).coerceIn(0, originalText.length)
    }
}