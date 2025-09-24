package com.eduardozanela.budget.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardozanela.budget.domain.Transaction
import com.eduardozanela.budget.domain.TransactionRepository
import com.eduardozanela.budget.domain.TransactionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _selectedMonth = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val selectedMonth: StateFlow<LocalDate> = _selectedMonth.asStateFlow()

    val transactions: StateFlow<List<Transaction>> =
        combine(
            _selectedCategory,
            _selectedMonth
        ) { category, month ->
            category to month
        }
            .flatMapLatest { (category, month) ->
                transactionRepository.searchTransaction(category, month) // Flow<List<Transaction>>
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Transformed state for the UI: Map of Date to List of Transactions
    val groupedTransactions: StateFlow<Map<LocalDate, List<Transaction>>> =
        transactions.map { transactions ->
            transactions
                .sortedByDescending { it.date } // Sort by date first (newest first for groups)
                .groupBy { it.date.date } // Group by date
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    // You can expose a list of available categories for the filter UI
    val availableCategories: Flow<List<String>> = transactionRepository.getAllTransactions()
        .map { transactions ->
            transactions.map { it.category }.distinct().sorted()
        }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)

    val totalIncome: StateFlow<Double> = transactions
        .map { list ->
            list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val totalExpenses: StateFlow<Double> = transactions
        .map { list ->
            // Assuming expenses are negative amounts, or you have a type field
            // If expenses are positive, use: list.filter { it.isExpense /* or similar */ }.sumOf { it.amount }
            // For this example, let's assume expenses are stored as positive values and there's a way to identify them,
            // or simply sum negative amounts if that's your convention.
            // Let's assume for now expenses are any amount < 0, and we take their absolute value for display.
            list.filter { it.type == TransactionType.EXPENSE }.sumOf { abs(it.amount) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    fun onMonthChange(newMonth: LocalDate) {
        _selectedMonth.value = newMonth
    }

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
    }

    fun clearFilters() {
        _selectedCategory.value = null
        _selectedMonth.value = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    fun deleteTransaction(id: String) {
        viewModelScope.launch {
            try {
                transactionRepository.deleteTransaction(id)
                _message.value = "Transaction deleted successfully."
            } catch (e: Exception) {
                _message.value = "Error deleting transaction: ${e.message}"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}