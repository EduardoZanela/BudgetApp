package com.eduardozanela.budget.presentation.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import budgetapp.composeapp.generated.resources.*
import budgetapp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@OptIn(ExperimentalResourceApi::class)
@Composable
fun rememberFireSansFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.FiraSans_Regular, FontWeight.Normal),
        Font(Res.font.FiraSans_Italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.FiraSans_Light, FontWeight.Light),
        Font(Res.font.FiraSans_LightItalic, FontWeight.Light, FontStyle.Italic),
        Font(Res.font.FiraSans_ExtraLight, FontWeight.ExtraLight),
        Font(Res.font.FiraSans_ExtraLightItalic, FontWeight.ExtraLight, FontStyle.Italic),
        Font(Res.font.FiraSans_Thin, FontWeight.Thin),
        Font(Res.font.FiraSans_ThinItalic, FontWeight.Thin, FontStyle.Italic),
        Font(Res.font.FiraSans_Medium, FontWeight.Medium),
        Font(Res.font.FiraSans_MediumItalic, FontWeight.Medium, FontStyle.Italic),
        Font(Res.font.FiraSans_Bold, FontWeight.Bold),
        Font(Res.font.FiraSans_ExtraBold, FontWeight.ExtraBold),
        Font(Res.font.FiraSans_BoldItalic, FontWeight.Bold, FontStyle.Italic),
        Font(Res.font.FiraSans_ExtraBoldItalic, FontWeight.ExtraBold, FontStyle.Italic)
    )
}