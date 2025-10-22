package com.eduardozanela.budget.presentation.core.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
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
import com.eduardozanela.budget.presentation.core.ui.BudgetAppTheme
import com.eduardozanela.budget.presentation.statement.screens.ImportStatement
import com.eduardozanela.budget.presentation.transaction.screens.AddTransactionScreen
import com.eduardozanela.budget.presentation.transaction.screens.TransactionListScreen
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun AppNavigationHost(
    modifier: Modifier,
    navController: NavHostController,
    onNavHostReady: suspend (NavController) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canNavigateBack = backStackEntry?.destination?.route != BudgetScreen.Transactions.name

    val isSelected: (name: String)-> Boolean = { name ->
        backStackEntry?.destination?.hierarchy?.any { it.route == name } == true
    }

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
//            topBar = {
//                Header(
//                    onHomeClick = {
//                        onClick(BudgetScreen.Transactions)
//                    },
//                    canNavigateBack = canNavigateBack,
//                    navigateUp = { navController.navigateUp() },
//                    showMenu = false
//                )
//            },
            modifier = modifier.padding(bottom = 10.dp),
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onClick(BudgetScreen.AddTransaction) },
                    containerColor = if(isSelected(BudgetScreen.AddTransaction.name)) MaterialTheme.colorScheme.primary.copy(alpha =0.5f) else MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.offset(y = 67.dp) // Adjust this value to position the FAB correctly
                ) {
                    Icon(
                        painter = painterResource(BudgetScreen.AddTransaction.icon),
                        contentDescription = stringResource(BudgetScreen.AddTransaction.title),
                        modifier = Modifier.size(30.dp)
                    )
                }
            },
            bottomBar = {
                Surface(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(25.dp),
                    tonalElevation = 6.dp, // gives floating effect
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 30.dp) // floating strip
                        .height(56.dp) // strip height, smaller than NavigationBar
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        BudgetScreen.entries.forEach { screen ->
                            if (screen.showMobile) {

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.weight(1f)
                                        .alpha(if (screen == BudgetScreen.AddTransaction) 0f else 1f)
                                        // Clicks are still registered when alpha is 0f, so only add clickable when visible
                                        .clickable(enabled = screen != BudgetScreen.AddTransaction) { onClick(screen) }
                                        .padding(vertical = 6.dp)

                                ) {
                                    Icon(
                                        painter = painterResource(screen.icon),
                                        tint = if (isSelected(screen.name)) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surface,
                                        contentDescription = screen.title.toString(),
                                        modifier = Modifier.size( 30.dp)
                                    )
                                    Text(
                                        text = stringResource(screen.title),
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = if (isSelected(screen.name)) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surface
                                    )
                                }
                            }
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