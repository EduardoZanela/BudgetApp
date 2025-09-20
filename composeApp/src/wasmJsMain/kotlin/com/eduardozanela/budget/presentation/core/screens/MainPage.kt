package com.eduardozanela.budget.presentation.core.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import budgetapp.composeapp.generated.resources.Res
import budgetapp.composeapp.generated.resources.svg_logo
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

/**
 * A main page composable that displays the app logo centered on the screen.
 * This can be used as a welcome or home screen.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun MainPage(
    onHomeClick: () -> Unit,
    onImportStatementClick: () -> Unit,
    onAddTransactionClick: () -> Unit,
    onListTransactionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // By using weighted spacers, we can push the content slightly above
        // the vertical center for a more balanced look.
        Spacer(Modifier.weight(0.2f))

        Image(
            painter = painterResource(Res.drawable.svg_logo),
            contentDescription = "Budget App Logo",
            modifier = Modifier.size(350.dp)
        )

        Spacer(Modifier.height(15.dp))

        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Button(onClick = onImportStatementClick) {
                Text("Home")
            }
            Spacer(Modifier.width(10.dp))
            Button(onClick = onImportStatementClick) {
                Text("Import Statement")
            }
            Spacer(Modifier.width(10.dp))
            Button(onClick = onAddTransactionClick) {
                Text("Add Transaction")
            }
            Spacer(Modifier.width(10.dp))
            Button(onClick = onListTransactionClick) {
                Text("Transactions")
            }
        }

        // This spacer has a slightly larger weight, pushing the content block up.
        Spacer(Modifier.weight(1.2f))
    }
}