package com.eduardozanela.budget.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import org.koin.compose.koinInject

@Composable
actual inline fun <reified T : ViewModel> getViewModel(): T = koinInject()