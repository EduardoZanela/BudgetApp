package com.eduardozanela.budget.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eduardozanela.budget.model.Record

@Composable
fun RecordTable(records: List<Record>, modifier: Modifier = Modifier, onUpdate: (Int, Record) -> Unit) {
    LazyColumn(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        items(records.size) { index ->
            val record = records[index]
            Row(Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                EditableField(
                    value = record.date,
                    onValueChange = { newValue -> onUpdate(index, record.copy(date = newValue)) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                EditableField(
                    value = record.description,
                    onValueChange = { newValue -> onUpdate(index, record.copy(description = newValue)) },
                    modifier = Modifier.weight(2f)
                )
                Spacer(Modifier.width(8.dp))
                EditableField(
                    value = record.amount.toString(),
                    onValueChange = { newValue -> onUpdate(index, record.copy(amount = newValue.toDoubleOrNull() ?: record.amount)) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun EditableField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
    )
}
