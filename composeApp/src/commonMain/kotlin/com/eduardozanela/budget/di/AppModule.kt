package com.eduardozanela.budget.di

import com.eduardozanela.budget.data.getTransactionRepository
import com.eduardozanela.budget.domain.TransactionRepository
import com.eduardozanela.budget.presentation.transaction.AddTransactionViewModel
import com.eduardozanela.budget.presentation.transaction.TransactionListViewModel
import com.eduardozanela.budget.presentation.transaction.screens.TransactionListScreen
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = module {
    single<TransactionRepository> { getTransactionRepository() }
    factory { AddTransactionViewModel(get()) }
    factory { TransactionListViewModel(get()) }
}

fun initKoin(appModule: Module) {
    startKoin {
        modules(
            appModule
        )
    }
}