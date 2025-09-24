package com.eduardozanela.budget

import android.app.Application
import com.eduardozanela.budget.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BudgetAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this

        startKoin {
            androidContext(this@BudgetAppApplication)
            modules(appModule)
        }
    }

    companion object {
        lateinit var instance: BudgetAppApplication
            private set
    }
}