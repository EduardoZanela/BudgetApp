package com.eduardozanela.budget.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header() {
    TopAppBar(
        title = { Text("Budget App") },
        actions = {
            IconButton(onClick = { /* Expand mobile menu in the future */ }) {
                Text("☰")
            }
        }
    )
}
