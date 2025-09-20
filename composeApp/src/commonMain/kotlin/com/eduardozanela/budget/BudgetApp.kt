package com.eduardozanela.budget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Publish
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import budgetapp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.StringResource
import budgetapp.composeapp.generated.resources.add_transaction_screen
import budgetapp.composeapp.generated.resources.import_screen
import budgetapp.composeapp.generated.resources.main_screen
import budgetapp.composeapp.generated.resources.transactions_screen
import com.eduardozanela.budget.presentation.core.screens.AppNavigationHost

/**
 * enum values that represent the screens in the app
 */
enum class BudgetScreen(val title: StringResource, val icon: ImageVector? = null) {
    Home(title = Res.string.main_screen),
    Transactions(title = Res.string.transactions_screen, icon = Icons.AutoMirrored.Outlined.List),
    AddTransaction(title = Res.string.add_transaction_screen, icon = Icons.Outlined.AddCircle),
    Import(title = Res.string.import_screen, icon = Icons.Outlined.Publish)
}

@Composable
fun BudgetApp(
    navController: NavHostController? = null,
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val controller = navController ?: rememberNavController()
    AppNavigationHost(navController = controller, onNavHostReady = onNavHostReady)
}