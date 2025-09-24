package com.eduardozanela.budget.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardozanela.budget.domain.Transaction
import com.eduardozanela.budget.domain.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flattenConcat
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

@OptIn(ExperimentalCoroutinesApi::class)
class SearchTransactionViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _startDate = MutableStateFlow<LocalDateTime?>(null)
    val startDate: StateFlow<LocalDateTime?> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<LocalDateTime?>(null)
    val endDate: StateFlow<LocalDateTime?> = _endDate.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _selectedMonth = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val selectedMonth: StateFlow<LocalDate> = _selectedMonth.asStateFlow()

    // Combined flow for filtering and searching
    init {
        // Collect all transactions, then apply filters and search
        combine(
            _searchQuery,
            _selectedCategory,
            _startDate,
            _endDate,
            _selectedMonth
        ) { query, category, start, end, month ->
            // Here, we call the repository with the latest filter values.
            // The repository handles the heavy lifting of filtering.
            // The result of this lambda will be a new Flow.
            transactionRepository.searchTransaction(query, category, start, end);
        }.flattenConcat()
            .onEach { filteredTransactions -> _transactions.value = filteredTransactions }
            .launchIn(viewModelScope) // Use launchIn for flows
    }

    fun onMonthChange(newMonth: LocalDate) {
        _selectedMonth.value = newMonth
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
    }

    fun onStartDateSelected(date: LocalDateTime?) {
        _startDate.value = date
    }

    fun onEndDateSelected(date: LocalDateTime?) {
        _endDate.value = date
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
        _startDate.value = null
        _endDate.value = null
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

    // You can expose a list of available categories for the filter UI
    val availableCategories: Flow<List<String>> = transactionRepository.getAllTransactions()
        .map { transactions ->
            transactions.map { it.category }.distinct().sorted()
        }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)

    // Transformed state for the UI: Map of Date to List of Transactions
    val groupedTransactions: StateFlow<Map<LocalDate, List<Transaction>>> =
        _transactions.map { transactions ->
            transactions
                .sortedByDescending { it.date } // Sort by date first (newest first for groups)
                .groupBy { it.date.date } // Group by date
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

}