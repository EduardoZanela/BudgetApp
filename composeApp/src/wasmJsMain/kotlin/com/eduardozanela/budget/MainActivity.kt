package com.eduardozanela.budget

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.eduardozanela.budget.di.appModule
import com.eduardozanela.budget.di.initKoin
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(appModule)
    ComposeViewport(document.body!!) {
        BudgetApp()
    }
}