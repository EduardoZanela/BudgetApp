package com.eduardozanela.budget.presentation.statement.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eduardozanela.budget.data.api.ApiClient
import com.eduardozanela.budget.presentation.components.BankSelector
import com.eduardozanela.budget.presentation.components.FilePicker
import com.eduardozanela.budget.presentation.components.RecordTable
import com.eduardozanela.budget.domain.Bank
import com.eduardozanela.budget.data.Record
import com.eduardozanela.budget.platform.downloadFile
import com.eduardozanela.budget.platform.getCurrentDateString
import com.eduardozanela.budget.utils.generateCsvFromRecords
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A sealed interface to represent the various states of the UI,
 * providing a more robust way to handle loading, success, and error feedback.
 */
sealed interface UiState {
    object Idle : UiState
    object Loading : UiState
    data class Success(val message: String) : UiState
    data class Error(val message: String) : UiState
}

@Composable
fun ImportStatement(modifier: Modifier = Modifier) {
    var records by remember { mutableStateOf(emptyList<Record>()) }
    var uiState by remember { mutableStateOf<UiState>(UiState.Idle) }
    var selectedBank by remember { mutableStateOf(Bank.NEO) }

    val scope = rememberCoroutineScope()
    val apiClient = remember { ApiClient() }

    LaunchedEffect(uiState) {
        if (uiState != UiState.Loading && uiState != UiState.Idle ) {
            delay(5000) // show for 5 seconds
            uiState = UiState.Idle
        }
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            BankSelector(
                selectedBank = selectedBank,
                onBankSelected = { bank ->
                    selectedBank = bank
                }
            )

            Spacer(Modifier.width(5.dp))
            FilePicker(
                onFileSelected = { fileBytes, fileName ->
                    uiState = UiState.Loading
                    scope.launch {
                        val result = apiClient.uploadFile(fileBytes, fileName, selectedBank)
                        result.fold(
                            onSuccess = { jsonRecords ->
                                records = jsonRecords
                                uiState = UiState.Success("Successfully loaded ${jsonRecords.size} records.")
                            },
                            onFailure = { error -> uiState = UiState.Error(error.message ?: "Unknown error") }
                        )
                    }
                }
            )

            // Show the export button only when there are records to export.
            if (records.isNotEmpty()) {
                Spacer(Modifier.width(5.dp))
                Button(
                    onClick = {
                        val csvData = generateCsvFromRecords(records)
                        val bankName = selectedBank.name
                        val currentDate = getCurrentDateString()
                        val fileName = "${bankName}-${currentDate}.csv"
                        downloadFile(fileName, csvData.encodeToByteArray(), "text/csv")
                    }
                ) {
                    Text("Export Edited CSV")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Display UI based on the current state
        when (val state = uiState) {
            is UiState.Loading -> {
                Text("Uploading...", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is UiState.Success -> {
                Text(state.message, modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp))
            }
            is UiState.Error -> {
                Text(state.message, modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp))
            }
            is UiState.Idle -> { /* No message in idle state */ }
        }

        // The RecordTable is now displayed unless there is an error, or it's loading.
        // It will show an empty list initially, and then the parsed records after a successful upload.
        if (uiState !is UiState.Loading && uiState !is UiState.Error) {
            RecordTable(
                records = records,
                modifier = Modifier.weight(1f)
            ) { index, updatedRecord ->
                records = records.mapIndexed { i, record -> if (i == index) updatedRecord else record }
            }
        }
    }
}