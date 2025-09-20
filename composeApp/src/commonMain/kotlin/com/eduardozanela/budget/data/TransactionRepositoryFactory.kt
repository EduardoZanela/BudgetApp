package com.eduardozanela.budget.data

import com.eduardozanela.budget.Database
import com.eduardozanela.budget.domain.TransactionRepository

/**
 * Expects a platform-specific function to provide an implementation of TransactionRepository.
 */
expect fun getTransactionRepository(): TransactionRepository