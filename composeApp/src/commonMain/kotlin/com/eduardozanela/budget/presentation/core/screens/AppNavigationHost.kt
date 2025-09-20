package com.eduardozanela.budget.presentation.core.screens

// src/commonMain/kotlin/com/eduardozanela/budget/AppNavigationHost.kt

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController

/**
 * Expected composable for the main application structure.
 * Android will implement this with a Scaffold and Bottom Navigation.
 * Wasm can implement it differently (e.g., simpler navigation or direct page rendering).
 */
@Composable
expect fun AppNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavHostReady: suspend (NavController) -> Unit
)