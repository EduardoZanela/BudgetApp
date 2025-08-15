package com.eduardozanela.budget

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BudgetApplication

fun main(args: Array<String>) {
    runApplication<BudgetApplication>(*args)
}
