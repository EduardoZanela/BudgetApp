package com.eduardozanela.budget

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import budgetapp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.StringResource
import budgetapp.composeapp.generated.resources.add
import budgetapp.composeapp.generated.resources.add_transaction_screen
import budgetapp.composeapp.generated.resources.budget
import budgetapp.composeapp.generated.resources.budget_screen
import budgetapp.composeapp.generated.resources.home
import budgetapp.composeapp.generated.resources.import
import budgetapp.composeapp.generated.resources.import_screen
import budgetapp.composeapp.generated.resources.main_screen
import budgetapp.composeapp.generated.resources.settings
import budgetapp.composeapp.generated.resources.settings_screen
import budgetapp.composeapp.generated.resources.transactions
import budgetapp.composeapp.generated.resources.transactions_screen
import com.eduardozanela.budget.presentation.core.screens.AppNavigationHost
import org.jetbrains.compose.resources.DrawableResource

/**
 * Enum values that represent the screens in the app
 */
enum class BudgetScreen(val title: StringResource, val icon: DrawableResource, val showMobile: Boolean = true) {
    Home(title = Res.string.main_screen, icon = Res.drawable.home, showMobile = false),
    Transactions(title = Res.string.transactions_screen, icon = Res.drawable.transactions),
    Budget(title = Res.string.budget_screen, icon = Res.drawable.budget),
    AddTransaction(title = Res.string.add_transaction_screen, icon = Res.drawable.add),
    Import(title = Res.string.import_screen, icon = Res.drawable.import),
    Settings(title = Res.string.settings_screen, icon = Res.drawable.settings)
}

@Composable
fun BudgetApp(
    navController: NavHostController? = null,
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val controller = navController ?: rememberNavController()
    AppNavigationHost(navController = controller, onNavHostReady = onNavHostReady)
}