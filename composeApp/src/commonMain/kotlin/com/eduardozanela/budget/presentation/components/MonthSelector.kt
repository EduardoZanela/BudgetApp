package com.eduardozanela.budget.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

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
                .background(MaterialTheme.colorScheme.background)
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
