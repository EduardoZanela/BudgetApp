package com.eduardozanela.budget.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import budgetapp.composeapp.generated.resources.Res
import budgetapp.composeapp.generated.resources.logo
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
    onImportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // By using weighted spacers, we can push the content slightly above
        // the vertical center for a more balanced look.
        Spacer(Modifier.weight(0.5f))

        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "Budget App Logo",
            modifier = Modifier.size(350.dp) // Reduced size for better balance on most screens
            // Using Image (instead of Icon) is correct for showing an SVG's original colors.
            // If it still appears black, the issue is likely within the logo.svg file itself.
            // Check if it uses `fill="currentColor"` and replace it with a specific hex color.
        )

        Spacer(Modifier.height(15.dp)) // Increased space for better visual separation

        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Button(onClick = onHomeClick) {
                Text("Home")
            }
            Spacer(Modifier.width(10.dp))
            Button(onClick = onImportClick) {
                Text("Import Statement")
            }
            Spacer(Modifier.width(10.dp))
            Button(onClick = onImportClick) {
                Text("Other Action")
            }
        }

        // This spacer has a slightly larger weight, pushing the content block up.
        Spacer(Modifier.weight(1.2f))
    }
}