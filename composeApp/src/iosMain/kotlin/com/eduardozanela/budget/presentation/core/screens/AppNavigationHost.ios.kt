package com.eduardozanela.budget.presentation.core.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
actual fun AppNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavHostReady: suspend (NavController) -> Unit
) {
    TODO("Not yet implemented")
}