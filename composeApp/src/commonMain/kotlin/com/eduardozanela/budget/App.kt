package com.eduardozanela.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

import com.eduardozanela.budget.api.ApiClient
import com.eduardozanela.budget.components.BankSelector
import com.eduardozanela.budget.components.Header
import com.eduardozanela.budget.components.RecordTable
import com.eduardozanela.budget.components.FilePicker
import com.eduardozanela.budget.model.Bank
import com.eduardozanela.budget.model.Record
import com.eduardozanela.budget.ui.BudgetAppTheme
import com.eduardozanela.budget.platform.getCurrentDateString
import com.eduardozanela.budget.platform.downloadFile
import com.eduardozanela.budget.utils.parseCsvToRecords
import com.eduardozanela.budget.utils.generateCsvFromRecords
import io.ktor.utils.io.core.toByteArray

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
fun App() {
    var records by remember { mutableStateOf(emptyList<Record>()) }
    var uiState by remember { mutableStateOf<UiState>(UiState.Idle) }
    var selectedBank by remember { mutableStateOf(Bank.NEO) }

    val scope = rememberCoroutineScope()
    val apiClient = remember { ApiClient() } // Create an instance of our client

    BudgetAppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Header()
            Spacer(Modifier.height(16.dp))
            BankSelector(
                selectedBank = selectedBank,
                onBankSelected = { bank ->
                    selectedBank = bank
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            FilePicker(
                onFileSelected = { fileBytes, fileName ->
                    uiState = UiState.Loading
                    scope.launch {
                        val result = apiClient.uploadFile(fileBytes, fileName, selectedBank)
                        result.fold(
                            onSuccess = { csvData ->
                                val parsedRecords = parseCsvToRecords(csvData)
                                records = parsedRecords
                                uiState = UiState.Success("Successfully loaded ${parsedRecords.size} records.")
                            },
                            onFailure = { error -> uiState = UiState.Error(error.message ?: "Unknown error") }
                        )
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Show the export button only when there are records to export.
            if (records.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        val csvData = generateCsvFromRecords(records)
                        val bankName = selectedBank.name
                        val currentDate = getCurrentDateString()
                        val fileName = "${bankName}-${currentDate}.csv"
                        downloadFile(fileName, csvData.encodeToByteArray(), "text/csv")
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Export Edited CSV")

                }
            }


            Spacer(Modifier.height(16.dp))

            // Display UI based on the current state
            when (val state = uiState) {
                is UiState.Loading -> {
                    Text("Uploading...", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is UiState.Success -> {
                    Text("✅ ${state.message}", modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp))
                }
                is UiState.Error -> {
                    Text("❌ ${state.message}", modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp))
                }
                is UiState.Idle -> { /* No message in idle state */ }
            }

            // The RecordTable is now displayed unless there is an error or it's loading.
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
}