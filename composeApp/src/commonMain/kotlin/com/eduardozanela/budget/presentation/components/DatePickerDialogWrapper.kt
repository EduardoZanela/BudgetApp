package com.eduardozanela.budget.presentation.components

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDateTime

@Composable
expect fun DatePickerDialogWrapper(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDateTime) -> Unit,
    initialDate: LocalDateTime
)
