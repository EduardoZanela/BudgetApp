package com.eduardozanela.budget.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardozanela.budget.domain.Transaction
import com.eduardozanela.budget.domain.TransactionRepository
import com.eduardozanela.budget.domain.TransactionType
import com.benasher44.uuid.uuid4
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

enum class AddTransactionErrorType {
    SUCCESS,
    EMPTY_AMOUNT,
    EMPTY_DESCRIPTION,
    EMPTY_CATEGORY,
    UNKNOWN_ERROR
}

data class TransactionError(var type: AddTransactionErrorType, var message: String)

class AddTransactionViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _type = MutableStateFlow(TransactionType.EXPENSE)
    val type: StateFlow<TransactionType> = _type.asStateFlow()

    private val _date = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    val date: StateFlow<LocalDateTime> = _date.asStateFlow()

    private val _accountId = MutableStateFlow("default_account_id") // TODO: get real account ID
    val accountId: StateFlow<String> = _accountId.asStateFlow()

    private val _message = MutableStateFlow<TransactionError?>(null)
    val message: StateFlow<TransactionError?> = _message.asStateFlow()

    fun onAmountChange(newAmount: String) {
        _message.value = null // Clear error message when amount changes
        _amount.value = newAmount
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun onCategoryChange(newCategory: String) {
        _category.value = newCategory
    }

    fun onTypeChange(newType: TransactionType) {
        _type.value = newType
    }

    fun onAccountChange(newAccount: String) {
        _accountId.value = newAccount
    }

    fun onDateChange(newDate: LocalDateTime) {
        _date.value = newDate
    }

    fun saveTransaction() {
        viewModelScope.launch {
            try {
                val parsedAmount = _amount.value.toDoubleOrNull()
                if (parsedAmount == null) {
                    _message.value = TransactionError(AddTransactionErrorType.EMPTY_AMOUNT, "Invalid amount")
                    return@launch
                }
                if (_description.value.isBlank()) {
                    _message.value = TransactionError(AddTransactionErrorType.EMPTY_DESCRIPTION, "Description cannot be empty")
                    return@launch
                }
                if (_category.value.isBlank()) {
                    _message.value = TransactionError(AddTransactionErrorType.EMPTY_CATEGORY, "Category cannot be empty")
                    return@launch
                }

                val newTransaction = Transaction(
                    id = uuid4().toString(), // Generate a unique ID
                    amount = parsedAmount,
                    description = _description.value,
                    category = _category.value,
                    type = _type.value,
                    date = _date.value,
                    accountId = _accountId.value
                )

                transactionRepository.addTransaction(newTransaction)

                _message.value = TransactionError(AddTransactionErrorType.SUCCESS, "Transaction saved successfully!")
                // Clear fields after saving
                _amount.value = ""
                _description.value = ""
                _category.value = ""
                _type.value = TransactionType.EXPENSE
                _date.value = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            } catch (e: Exception) {
                _message.value = TransactionError(AddTransactionErrorType.UNKNOWN_ERROR, "Error saving transaction: ${e.message}")
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}