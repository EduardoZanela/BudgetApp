package com.eduardozanela.budget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import budgetapp.composeapp.generated.resources.Montserrat_Bold
import budgetapp.composeapp.generated.resources.Montserrat_Regular
import budgetapp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@OptIn(ExperimentalResourceApi::class)
@Composable
fun rememberMontserratFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.Montserrat_Regular, FontWeight.Normal),
        Font(Res.font.Montserrat_Bold, FontWeight.Bold)
    )
}