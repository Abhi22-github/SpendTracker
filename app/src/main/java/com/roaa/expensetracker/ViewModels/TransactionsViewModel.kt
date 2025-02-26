package com.roaa.expensetracker.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Database.TransactionRepository
import com.roaa.expensetracker.Model.TotalAmountClass
import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import com.roaa.expensetracker.Model.TransactionClass
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.Constants.INCOME
import com.roaa.expensetracker.Utilities.UiState
import com.roaa.expensetracker.Utilities.toLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    //for opening bottom sheet in main activity
    var bottomSheetStatus = MutableStateFlow<Boolean>(false)

    //flow for Ui states
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    //flow to get total Expense amount for range
    private val _getTotalExpenseAmountForRangeFlow =
        MutableStateFlow<TotalAmountClass>(TotalAmountClass(0, 0f))
    val getTotalExpenseAmountForRangeFlow: StateFlow<TotalAmountClass> =
        _getTotalExpenseAmountForRangeFlow

    //flow to get total Income amount for range
    private val _getTotalIncomeAmountForRangeFlow =
        MutableStateFlow<TotalAmountClass>(TotalAmountClass(0, 0f))
    val getTotalIncomeAmountForRangeFlow: StateFlow<TotalAmountClass> =
        _getTotalIncomeAmountForRangeFlow

    //flow to get all transactions for give date
    private val _getAllTransactionsForDateFlow =
        MutableStateFlow<List<TransactionWithDetails>>(listOf())
    val getAllTransactionsForDateFlow: StateFlow<List<TransactionWithDetails>> =
        _getAllTransactionsForDateFlow

    //flow to get list of TotalAmount
    private val _getListOfTotalAmountPerDayForRangeFlow =
        MutableStateFlow<List<TotalExpenseIncomeClass>>(listOf())
    val getListOfTotalAmountPerDayForRangeFlow: StateFlow<List<TotalExpenseIncomeClass>> =
        _getListOfTotalAmountPerDayForRangeFlow

    //flow to get total amount for given date
    private val _getTotalExpenseAmountForDateFlow = MutableStateFlow<Float>(0f)
    val getTotalExpenseAmountForDateFlow: StateFlow<Float> = _getTotalExpenseAmountForDateFlow


    var selectedDate: Long
        private set
    private val dateFormatter: DateTimeFormatter? = null
    var transactionTypeFromViewModel: String
        private set
    private val _currentSelectedDate: MutableLiveData<Int>
    var currentSelectedDate: LiveData<Int>
    var todaysDate: LocalDate
    var spend: MutableLiveData<BigDecimal>
    var oldTotal: MutableLiveData<Float>
    var newTotal: MutableLiveData<Float>
    var oldTotalString: MutableLiveData<String>
    var newTotalString: MutableLiveData<String>
    var dateList: MutableLiveData<MutableList<String>>


    init {
        selectedDate = Calendar.getInstance().timeInMillis
        transactionTypeFromViewModel = EXPENSE
        _currentSelectedDate = MutableLiveData(0)
        currentSelectedDate = _currentSelectedDate
        todaysDate = LocalDate.now()
        spend = MutableLiveData(BigDecimal.valueOf(1))
        oldTotal = MutableLiveData(1f)
        newTotal = MutableLiveData(1f)
        oldTotalString = MutableLiveData("")
        newTotalString = MutableLiveData("")
        dateList = MutableLiveData(mutableListOf<String>())
    }


    fun validateAndPrepareTransactionData(
        selectedType: String,
        selectedCategoryId: Long,
        expenseValue: String,
        comment: String,
        selectedDate: Long?,
        selectedBankAccountId: Long,
    ) {
        storeFormDataInDatabase(
            expense = selectedType,
            amount = expenseValue,
            note = comment,
            date = selectedDate ?: LocalDate.now().toLong(),
            selectedCategoryId,
            selectedBankAccountId,
        )
    }


    private fun storeFormDataInDatabase(
        expense: String,
        amount: String,
        note: String,
        date: Long,
        selectedCategoryId: Long,
        selectedBankAccountId: Long
    ) {
        // on below line we are creating
        // a variable for our modal class.
        val modal = TransactionClass(
            id = 0L,
            type = expense.trim { it <= ' ' },
            amount = amount.trim { it <= ' ' }.replace(",", "").toFloat(),
            note = note.trim { it <= ' ' },
            dateWithTime = System.currentTimeMillis(),
            date = date,
            categoryId = selectedCategoryId,
            bankAccountId = selectedBankAccountId
        )

        viewModelScope.launch { transactionRepository.insertAndPropagateChanges(modal) }
    }

    fun updateFormDataInDatabase(transactionClass: TransactionClass) {
        viewModelScope.launch { transactionRepository.updateAndPropagateChanges(transactionClass) }
    }

    fun saveSelectedDate(date: Long) {
        this.selectedDate = date
    }

    val allTransactions: Flow<List<TransactionWithDetails>>
        get() = transactionRepository.allTransactions

    fun getTotalTransactionForMonth(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithDetails>> {
        return transactionRepository.getTotalTransactionForMonth(startDate, endDate)
    }


    fun getTotalIncomeForRange(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            loading()
            getTotalAmountByDateRangeAndCategoryType(
                startDate, endDate, INCOME
            ).catch { e ->
                error(e)
            }.collect { totalAmount ->
                _getTotalIncomeAmountForRangeFlow.value = totalAmount
                Log.d("Hello", "getTotalIncomeForRange: $totalAmount")
                completed()
            }
        }
    }

    fun getTotalExpenseForRange(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            loading()
            getTotalAmountByDateRangeAndCategoryType(
                startDate, endDate, EXPENSE
            ).catch { error(it) }.collect { totalAmount ->
                _getTotalExpenseAmountForRangeFlow.value = totalAmount
                Log.d("Hello", "getTotalExpenseForRange: $totalAmount")
                completed()
            }
        }
    }


    fun getTotalAmountByDateRangeAndCategoryType(
        startDate: Long, endDate: Long, categoryType: String
    ): Flow<TotalAmountClass> {
        return transactionRepository.getTotalAmountByDateRangeAndCategoryType(
            startDate, endDate, categoryType
        )
    }


    fun getListOfTotalAmountPerDayForRange(
        startDate: Long, endDate: Long
    ) {
        viewModelScope.launch {
            loading()
            transactionRepository.getListOfTotalAmountPerDayForRange(startDate, endDate)
                .catch { error(it) }.collect { totalAmountList ->
                    _getListOfTotalAmountPerDayForRangeFlow.value = totalAmountList
                    completed()
                }
        }
    }

    fun getListOfTotalAmountPerDayForRangeForCompose(
        startDate: Long, endDate: Long
    ): Flow<List<TotalExpenseIncomeClass>> {
        return transactionRepository.getListOfTotalAmountPerDayForRange(startDate, endDate)
    }
    fun getListOfTotalAmountPerDayForRangeForComposeForBankAccountId(
        startDate: Long, endDate: Long,bankAccountId: Long
    ): Flow<List<TotalExpenseIncomeClass>> {
        return transactionRepository.getListOfTotalAmountPerDayForRangeForBankAccountId(startDate, endDate, bankAccountId)
    }

    fun deleteSingleTransaction(transactionClass: TransactionClass) {
        viewModelScope.launch {
            loading()
            //transactionRepository.delete(transactionClass)
            transactionRepository.deleteAndPropagateChanges(transactionClass)
            completed()
        }
    }

    fun getAllTransactionsForDate(date: Long) {
        viewModelScope.launch {
            loading()
            transactionRepository.getAllTransactionsForDate(date).catch { error(it) }
                .collect { transactionsList ->
                    _getAllTransactionsForDateFlow.value = transactionsList
                    completed()
                }
        }
    }


    fun getAllTransactionsForDateCompose(date: Long): Flow<List<TransactionWithDetails>> {

        return transactionRepository.getAllTransactionsForDate(date)
    }

    fun getTotalExpenseAmountForDate(date: Long) {
        viewModelScope.launch {
            loading()
            val flow = transactionRepository.getTotalAmountForDate(date, EXPENSE)

            flow.let {
                it.catch {
                    error(it)
                }.collect { amount ->
                    if (amount != null) _getTotalExpenseAmountForDateFlow.value = amount
                    completed()
                }
            }

        }
    }

    fun getTransactionsListForBankAccountId(bankAccountId:Long): Flow<List<TransactionWithDetails>> {
        return transactionRepository.getTransactionListForBankAccountId(bankAccountId)
    }

    fun getTotalExpenseAmountForDateCompose(date: Long): Flow<Float> {
        return transactionRepository.getTotalAmountForDate(date, EXPENSE)
    }

    fun getTotalIncomeAmountForDateCompose(date: Long): Flow<Float> {
        return transactionRepository.getTotalAmountForDate(date, INCOME)
    }

    fun getTotalExpenseAmountForDateExcludingLastCompose(date: Long): Flow<Float> {
        return transactionRepository.getTotalAmountForDateExcludingLast(date, EXPENSE)
    }

    fun setCurrentSelectedDate(currentSelectedDate: LocalDate?) {
        _currentSelectedDate.value = diffBetCurrentAndGivenDate(currentSelectedDate)
        this.currentSelectedDate = _currentSelectedDate
    }

    fun diffBetCurrentAndGivenDate(currentSelectedDate: LocalDate?): Int {
        return if (todaysDate.compareTo(currentSelectedDate) > 0) {
            -ChronoUnit.DAYS.between(todaysDate, currentSelectedDate).toInt()
        } else if (todaysDate.compareTo(currentSelectedDate) > 0) {
            ChronoUnit.DAYS.between(todaysDate, currentSelectedDate).toInt()
        } else {
            0
        }
    }

    fun loading() {
        _uiState.value = UiState.Loading
    }

    fun completed() {
        _uiState.value = UiState.Success
    }

    fun error(error: Throwable) {
        _uiState.value = UiState.Error(error.toString())
        Log.d("Hello Error reason", error.toString())
    }
}
