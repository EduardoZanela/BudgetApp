package com.eduardozanela.budget.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A platform-agnostic composable that allows the user to select a file.
 * The actual implementation will be provided by each platform (e.g., wasmJs, android).
 */
@Composable
expect fun FilePicker(
    onFileSelected: (ByteArray, String) -> Unit,
    modifier: Modifier = Modifier
)