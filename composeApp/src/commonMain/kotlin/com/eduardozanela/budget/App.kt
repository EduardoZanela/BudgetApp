package com.eduardozanela.budget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import budgetapp.composeapp.generated.resources.Res
import budgetapp.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    var records by remember { mutableStateOf(sampleRecords()) }
    var loading by remember { mutableStateOf(false) }
    var csvContent by remember { mutableStateOf<String?>(null) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Header()

            Spacer(Modifier.height(16.dp))

            FileUploader {
                loading = true
                MainScope().launch {
                    // Simulate processing
                    kotlinx.coroutines.delay(1000)
                    loading = false
                }
            }

            Spacer(Modifier.height(16.dp))

            if (loading) {
                Text("Processing PDF...", modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                RecordTable(records) { updated -> records = updated }
            }

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                csvContent = buildCsv(records)
            }) {
                Text("Export to CSV")
            }

            csvContent?.let {
                Spacer(Modifier.height(8.dp))
                Text("CSV Generated (simulate download):\n$it", modifier = Modifier.padding(16.dp))
            }
        }
    }
}