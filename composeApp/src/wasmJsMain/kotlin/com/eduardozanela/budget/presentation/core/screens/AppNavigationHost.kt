package com.eduardozanela.budget.presentation.core.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.eduardozanela.budget.BudgetScreen
import com.eduardozanela.budget.presentation.components.Header
import com.eduardozanela.budget.presentation.core.ui.BudgetAppTheme
import com.eduardozanela.budget.presentation.statement.screens.ImportStatement
import com.eduardozanela.budget.presentation.transaction.screens.AddTransactionScreen
import com.eduardozanela.budget.presentation.transaction.screens.TransactionListScreen

@Composable
actual fun AppNavigationHost(
    modifier: Modifier,
    navController: NavHostController,
    onNavHostReady: suspend (NavController) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canNavigateBack = backStackEntry?.destination?.route != BudgetScreen.Home.name

    val onClick: (screen: BudgetScreen) -> Unit = { screen ->
        navController.navigate(screen.name) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    BudgetAppTheme {
        Scaffold(
            topBar = {
                Header(
                    onHomeClick = { onClick(BudgetScreen.Home) },
                    canNavigateBack = canNavigateBack,
                    navigateUp = { navController.navigateUp() }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BudgetScreen.Home.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = BudgetScreen.Home.name) {
                    MainPage(
                        onHomeClick = { onClick(BudgetScreen.Home) },
                        onImportStatementClick = { onClick(BudgetScreen.Import) },
                        onAddTransactionClick = { onClick(BudgetScreen.AddTransaction) },
                        onListTransactionClick = { onClick(BudgetScreen.Transactions) },
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
                }

                composable(route = BudgetScreen.Import.name) {
                    ImportStatement(
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
                }

                composable(route = BudgetScreen.AddTransaction.name) {
                    AddTransactionScreen(
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
                }

                composable(route = BudgetScreen.Transactions.name) {
                    TransactionListScreen(
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
                }
            }
            LaunchedEffect(navController) {
                onNavHostReady(navController)
            }

        }
    }
}