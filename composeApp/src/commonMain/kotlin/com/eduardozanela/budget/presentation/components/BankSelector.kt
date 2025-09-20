package com.eduardozanela.budget.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eduardozanela.budget.domain.Bank

@Composable
fun BankSelector(
    selectedBank: Bank,
    onBankSelected: (Bank) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        Button(onClick = { expanded = true }) {
            Text(selectedBank.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Bank.entries.forEach { bank ->
                DropdownMenuItem(text = { Text(bank.name) }, onClick = {
                    onBankSelected(bank)
                    expanded = false
                })
            }
        }
    }
}