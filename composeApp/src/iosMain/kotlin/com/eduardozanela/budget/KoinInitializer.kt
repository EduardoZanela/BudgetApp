package com.eduardozanela.budget

import com.eduardozanela.budget.di.appModule
import com.eduardozanela.budget.di.initKoin

fun initKoin() {
    initKoin(appModule)
}