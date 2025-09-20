package com.eduardozanela.budget.presentation.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlinx.datetime.LocalDateTime

@Composable
actual fun DatePickerDialogWrapper(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDateTime) -> Unit,
    initialDate: LocalDateTime
) {
    val context = LocalContext.current

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = LocalDateTime(year, month + 1, dayOfMonth, 0, 0)
            onDateSelected(selectedDate)
        },
        initialDate.year,
        initialDate.monthNumber - 1, // Month is 0-indexed in DatePickerDialog
        initialDate.dayOfMonth
    )

    datePickerDialog.setOnDismissListener {
        onDismissRequest()
    }

    // Show the dialog
    if (!datePickerDialog.isShowing) {
        datePickerDialog.show()
    }
}