package com.roaa.expensetracker.ViewModels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.chip.Chip
import com.roaa.expensetracker.Database.TransactionRepository
import com.roaa.expensetracker.Events.EventMessage
import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.Model.TotalAmountClass
import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import com.roaa.expensetracker.Model.TransactionClass
import com.roaa.expensetracker.Utilities.Constants
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.LocalDateToLong
import com.roaa.expensetracker.Utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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
        MutableStateFlow<TotalAmountClass>(TotalAmountClass(0, 0))
    val getTotalExpenseAmountForRangeFlow: StateFlow<TotalAmountClass> =
        _getTotalExpenseAmountForRangeFlow

    //flow to get total Income amount for range
    private val _getTotalIncomeAmountForRangeFlow =
        MutableStateFlow<TotalAmountClass>(TotalAmountClass(0, 0))
    val getTotalIncomeAmountForRangeFlow: StateFlow<TotalAmountClass> =
        _getTotalIncomeAmountForRangeFlow

    //flow to get all transactions for give date
    private val _getAllTransactionsForDateFlow = MutableStateFlow<List<TransactionClass>>(listOf())
    val getAllTransactionsForDateFlow: StateFlow<List<TransactionClass>> =
        _getAllTransactionsForDateFlow

    //flow to get list of TotalAmount
    private val _getListOfTotalAmountPerDayForRangeFlow =
        MutableStateFlow<List<TotalExpenseIncomeClass>>(listOf())
    val getListOfTotalAmountPerDayForRangeFlow: StateFlow<List<TotalExpenseIncomeClass>> =
        _getListOfTotalAmountPerDayForRangeFlow

    //flow to get total amount for given date
    private val _getTotalExpenseAmountForDateFlow = MutableStateFlow<Long>(0L)
    val getTotalExpenseAmountForDateFlow: StateFlow<Long> = _getTotalExpenseAmountForDateFlow


    private val chipsName = arrayOf<String>()
    private val chipsList: List<Chip> = ArrayList()
    private var currentSelectedExpenseCategory = ""
    private var currentSelectedIncomeCategory = ""
    private val amount = 0
    private val note = ""
    var selectedDate: Long
        private set
    private val dateFormatter: DateTimeFormatter? = null
    var transactionTypeFromViewModel: String
        private set
    private var category: String? = null
    private val sharedPreferences: SharedPreferences? = null

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

    fun storeCurrentExpenseSelectChip(s: String) {
        currentSelectedExpenseCategory = s
    }

    fun storeCurrentIncomeSelectChip(s: String) {
        currentSelectedIncomeCategory = s
    }

    fun setTransactionTypeInViewModel(transaction: String) {
        transactionTypeFromViewModel = transaction
    }

    fun validateAndPrepareTransactionData(
        selectedType: String,
        selectedCategory: CategoryClass,
        expenseValue: String,
        comment: String,
        selectedDate: Long?,
        selectedPaymentMethod: String
    ) {
        storeFormDataInDatabase(
            expense = selectedType,
            amount = expenseValue,
            note = comment,
            category = selectedCategory.categoryName,
            categoryIcon = selectedCategory.categoryIconNumber,
            dateWithTime = selectedDate ?: LocalDateToLong(LocalDate.now()),
            selectedPaymentMethod = selectedPaymentMethod
        )
    }

    fun validateFormData(amount: String, note: String, mContext: Context?) {
        //to get,separate and validate data from both expense and income
        if (amount.isEmpty()) {
            EventBus.getDefault().post(EventMessage(4, "Amount can't be zero"))
        } else if (transactionTypeFromViewModel == EXPENSE) {
            category = currentSelectedExpenseCategory
            if (currentSelectedExpenseCategory.isEmpty()) {
                EventBus.getDefault().post(EventMessage(5, "Please select a category"))
            } else {
                //store the data
                Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show()
                storeFormDataInDatabase(
                    transactionTypeFromViewModel, amount, note, category, 1, selectedDate, "Cash"
                )
            }
        } else if (transactionTypeFromViewModel == Constants.INCOME) {
            category = currentSelectedIncomeCategory
            if (currentSelectedIncomeCategory.isEmpty()) {
                EventBus.getDefault().post(EventMessage(5, "Please select a category"))
            } else {
                //store the data
                Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show()
                storeFormDataInDatabase(
                    transactionTypeFromViewModel, amount, note, category, 1, selectedDate, "Cash"
                )
            }
        }
    }


    private fun storeFormDataInDatabase(
        expense: String,
        amount: String,
        note: String,
        category: String?,
        categoryIcon: Int,
        dateWithTime: Long,
        selectedPaymentMethod: String
    ) {
        // on below line we are creating
        // a variable for our modal class.
        val modal = TransactionClass()
        modal.amount = amount.trim { it <= ' ' }.replace(",", "").toLong()
        modal.type = expense.trim { it <= ' ' }
        modal.note = note.trim { it <= ' ' }
        modal.category = category!!.trim { it <= ' ' }
        modal.categoryIcon = categoryIcon
        modal.dateWithTime = dateWithTime
        val date = Instant.ofEpochMilli(dateWithTime).atZone(ZoneId.systemDefault()) // default zone
            .toLocalDate()
        modal.date = date.toString().replace("-", "").toLong()
        modal.paymentMethod = selectedPaymentMethod


        //   Log.d("date in local date" ,d.toString());
        viewModelScope.launch { transactionRepository.insert(modal) }

        EventBus.getDefault().post(EventMessage(6, "success"))

        //  liveData.postValue("something");
    }

    fun saveSelectedDate(date: Long) {
        this.selectedDate = date
    }

    val allTransactions: Flow<List<TransactionClass>>
        get() = transactionRepository.allTransactions


    fun getTotalIncomeForRange(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            loading()
            getTotalAmountByDateRangeAndCategoryType(
                startDate, endDate, Constants.INCOME
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


    suspend fun getTotalAmountByDateRangeAndCategoryType(
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

    fun deleteSingleTransaction(transactionClass: TransactionClass) {
        viewModelScope.launch {
            loading()
            transactionRepository.delete(transactionClass)
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
