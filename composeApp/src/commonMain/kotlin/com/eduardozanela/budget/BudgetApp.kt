package com.eduardozanela.budget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import budgetapp.composeapp.generated.resources.Res
import com.eduardozanela.budget.components.Header
import org.jetbrains.compose.resources.StringResource
import androidx.navigation.compose.composable
import budgetapp.composeapp.generated.resources.import_screen
import budgetapp.composeapp.generated.resources.main_screen
import com.eduardozanela.budget.screens.ImportStatement
import com.eduardozanela.budget.screens.MainPage
import com.eduardozanela.budget.ui.BudgetAppTheme

/**
 * enum values that represent the screens in the app
 */
enum class BudgetScreen(val title: StringResource) {
    Main(title = Res.string.main_screen),
    Import(title = Res.string.import_screen)
}

@Composable
fun BudgetApp(
    navController: NavHostController? = null,
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val controller = navController ?: rememberNavController()
    val backStackEntry by controller.currentBackStackEntryAsState()
    val canNavigateBack = backStackEntry?.destination?.route != BudgetScreen.Main.name

    BudgetAppTheme {
        Scaffold(
            topBar = {
                Header(
                    onHomeClick = { controller.navigate(BudgetScreen.Main.name) },
                    canNavigateBack = canNavigateBack,
                    navigateUp = { controller.navigateUp() }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = controller,
                startDestination = BudgetScreen.Main.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = BudgetScreen.Main.name) {
                    MainPage(
                        { controller.navigate(BudgetScreen.Main.name) },
                        { controller.navigate(BudgetScreen.Import.name) },
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
                }
                composable(route = BudgetScreen.Import.name) {
                    ImportStatement(
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
                }
            }
            LaunchedEffect(controller) {
                onNavHostReady(controller)
            }

        }
    }
}