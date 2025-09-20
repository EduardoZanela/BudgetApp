package com.eduardozanela.budget.presentation.core.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
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
    val canNavigateBack = backStackEntry?.destination?.route != BudgetScreen.Transactions.name

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
                    onHomeClick = {
                        onClick(BudgetScreen.Transactions)
                    },
                    canNavigateBack = canNavigateBack,
                    navigateUp = { navController.navigateUp() },
                    showMenu = false
                )
            },
            bottomBar = {
                NavigationBar (
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                ) {
                    val currentDestination = backStackEntry?.destination
                    BudgetScreen.entries.forEach { screen ->
                        if( screen.icon != null ) {

                            val selected = currentDestination?.hierarchy?.any { it.route == screen.name } == true
                            NavigationBarItem(
                                icon = {
                                    Icon(screen.icon, contentDescription = screen.title.toString())
                                },
                                selected = selected,
                                onClick = {
                                    onClick(screen)
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent,
                                    selectedIconColor = MaterialTheme.colorScheme.surface,
                                    unselectedIconColor = MaterialTheme.colorScheme.surface
                                ),
                                modifier = (if (selected) Modifier.topBorder(
                                        width = 4.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    ) else Modifier)

                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BudgetScreen.Transactions.name,
                modifier = modifier.padding(innerPadding)
            ) {
               composable(route = BudgetScreen.Import.name) {
                    ImportStatement(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                composable(route = BudgetScreen.AddTransaction.name) {
                    AddTransactionScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                composable(route = BudgetScreen.Transactions.name) {
                    TransactionListScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            LaunchedEffect(navController) {
                onNavHostReady(navController)
            }

        }
    }
}

@SuppressLint("SuspiciousModifierThen")
@NonRestartableComposable
fun Modifier.topBorder(width: Dp, color: Color) = then(
    drawBehind {
        val strokeWidth = width.toPx()
        val y = 0f
        val a = (size.width - 34.dp.toPx()) / 2
        drawLine(color, Offset(a - 1, y), Offset(size.width - a + 1 , y), strokeWidth)
    }
)