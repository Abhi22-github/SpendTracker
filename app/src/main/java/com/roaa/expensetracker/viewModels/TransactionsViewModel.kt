package com.roaa.expensetracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.database.TransactionRepository
import com.roaa.expensetracker.database.relations.TransactionWithDetails
import com.roaa.expensetracker.model.TransactionClass
import com.roaa.expensetracker.model.uiDataModels.InfoStatClass
import com.roaa.expensetracker.model.uiDataModels.TotalAmountClass
import com.roaa.expensetracker.model.uiDataModels.TotalExpenseIncomeClass
import com.roaa.expensetracker.utilities.Constants.EXPENSE
import com.roaa.expensetracker.utilities.Constants.INCOME
import com.roaa.expensetracker.utilities.UiState
import com.roaa.expensetracker.utilities.toLong
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
        MutableStateFlow<TotalAmountClass>(TotalAmountClass(0, BigDecimal.ZERO))
    val getTotalExpenseAmountForRangeFlow: StateFlow<TotalAmountClass> =
        _getTotalExpenseAmountForRangeFlow

    //flow to get total Income amount for range
    private val _getTotalIncomeAmountForRangeFlow =
        MutableStateFlow<TotalAmountClass>(TotalAmountClass(0, BigDecimal.ZERO))
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
    private val _getTotalExpenseAmountForDateFlow = MutableStateFlow<BigDecimal>(BigDecimal.ZERO)
    val getTotalExpenseAmountForDateFlow: StateFlow<BigDecimal> = _getTotalExpenseAmountForDateFlow


    var selectedDate: Long
        private set
    private val dateFormatter: DateTimeFormatter? = null
    var transactionTypeFromViewModel: String
        private set
    private val _currentSelectedDate: MutableLiveData<Int>
    var currentSelectedDate: LiveData<Int>
    var todaysDate: LocalDate
    var spend: MutableLiveData<BigDecimal>
    var oldTotal: MutableLiveData<BigDecimal>
    var newTotal: MutableLiveData<BigDecimal>
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
        oldTotal = MutableLiveData(BigDecimal.ONE)
        newTotal = MutableLiveData(BigDecimal.ONE)
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
        selectedTimeInMillis:Long,
        selectedBankAccountId: Long,
    ) {
        storeFormDataInDatabase(
            expense = selectedType,
            amount = expenseValue,
            note = comment,
            date = selectedDate ?: LocalDate.now().toLong(),
            selectedTimeInMillis = selectedTimeInMillis,
            selectedCategoryId = selectedCategoryId,
            selectedBankAccountId =selectedBankAccountId,
        )
    }


    private fun storeFormDataInDatabase(
        expense: String,
        amount: String,
        note: String,
        date: Long,
        selectedTimeInMillis : Long,
        selectedCategoryId: Long,
        selectedBankAccountId: Long
    ) {
        // on below line we are creating
        // a variable for our modal class.
        val modal = TransactionClass(
            id = 0L,
            type = expense.trim { it <= ' ' },
            amount = amount.trim { it <= ' ' }.replace(",", "").toBigDecimal(),
            note = note.trim { it <= ' ' },
            dateWithTime = selectedTimeInMillis,
            date = date,
            includeInRespectiveBudget = true,
            categoryId = selectedCategoryId,
            bankAccountId = selectedBankAccountId
        )

        viewModelScope.launch { transactionRepository.insertAndPropagateChanges(modal) }
    }

    fun updateFormDataInDatabase(transactionClass: TransactionClass) {
        viewModelScope.launch { transactionRepository.updateAndPropagateChanges(transactionClass) }
    }

    fun updateForBudgetSwitchDataInDatabase(transactionClass: TransactionClass) {
        viewModelScope.launch {
            transactionRepository.updateForBudgetSwitchAndPropagateChanges(
                transactionClass
            )
        }
    }

    fun saveSelectedDate(date: Long) {
        this.selectedDate = date
    }

    val allTransactions: Flow<List<TransactionWithDetails>>
        get() = transactionRepository.allTransactions

    fun getTotalTransactionForPeriod(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithDetails>> {
        return transactionRepository.getTotalTransactionForPeriod(startDate, endDate)
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

    fun getTotalAmountByDateRangeCategoryTypeAndBudgetStatus(
        startDate: Long, endDate: Long, categoryType: String, budgetStatus: Boolean
    ): Flow<TotalAmountClass> {
        return transactionRepository.getTotalAmountByDateRangeCategoryTypeAndBudgetStatus(
            startDate, endDate, categoryType, budgetStatus
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
        startDate: Long, endDate: Long, bankAccountId: Long
    ): Flow<List<TotalExpenseIncomeClass>> {
        return transactionRepository.getListOfTotalAmountPerDayForRangeForBankAccountId(
            startDate,
            endDate,
            bankAccountId
        )
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

    fun getTransactionsListForBankAccountId(bankAccountId: Long): Flow<List<TransactionWithDetails>> {
        return transactionRepository.getTransactionListForBankAccountId(bankAccountId)
    }

    fun getTotalExpenseAmountForDateCompose(date: Long): Flow<BigDecimal> {
        return transactionRepository.getTotalAmountForDate(date, EXPENSE)
    }

    fun getTotalIncomeAmountForDateCompose(date: Long): Flow<BigDecimal> {
        return transactionRepository.getTotalAmountForDate(date, INCOME)
    }

    fun getTotalExpenseAmountForDateExcludingLastCompose(date: Long): Flow<BigDecimal> {
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

    fun getSpecificCategoryStatistics(
        categoryId: Long,
        startDate: Long,
        endDate: Long
    ): Flow<InfoStatClass> {
        return transactionRepository.getSpecificCategoryStatistics(categoryId, startDate, endDate)
    }

    fun getSpecificBankStatistics(
        bankId: Long,
        startDate: Long,
        endDate: Long
    ): Flow<InfoStatClass> {
        return transactionRepository.getSpecificBankStatistics(bankId, startDate, endDate)
    }

    fun loading() {
        _uiState.value = UiState.Loading
    }

    fun completed() {
        _uiState.value = UiState.Success
    }

    fun error(error: Throwable) {
        _uiState.value = UiState.Error(error.toString())
    }
}
