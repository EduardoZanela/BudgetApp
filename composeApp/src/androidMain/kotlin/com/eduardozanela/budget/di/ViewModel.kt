package com.eduardozanela.budget.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
actual inline fun <reified T : ViewModel> getViewModel(): T = koinViewModel()