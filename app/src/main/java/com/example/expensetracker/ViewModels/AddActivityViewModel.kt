package com.example.expensetracker.ViewModels

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.expensetracker.Database.CategoryRepository
import com.example.expensetracker.Database.TransactionRepository
import com.example.expensetracker.Events.EventMessage
import com.example.expensetracker.Model.CategoryClass
import com.example.expensetracker.Model.TotalAmountClass
import com.example.expensetracker.Model.TotalExpenseIncomeClass
import com.example.expensetracker.Model.TransactionClass
import com.example.expensetracker.Utilities.Constants
import com.google.android.material.chip.Chip
import dagger.hilt.android.lifecycle.HiltViewModel
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
class AddActivityViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
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


    init {
        selectedDate = Calendar.getInstance().timeInMillis
        transactionTypeFromViewModel = Constants.EXPENSE
        _currentSelectedDate = MutableLiveData(0)
        currentSelectedDate = _currentSelectedDate
        todaysDate = LocalDate.now()
        spend = MutableLiveData(BigDecimal.valueOf(1))
        oldTotal = MutableLiveData(1f)
        newTotal = MutableLiveData(1f)
        oldTotalString = MutableLiveData("")
        newTotalString = MutableLiveData("")
    }

    val categoryNames: LiveData<List<CategoryClass>>
        get() = categoryRepository.allCategories

    val onlyIncomeCategoryNames: LiveData<List<CategoryClass>>
        get() = categoryRepository.onlyIncomeCategories

    val onlyExpenseCategoryNames: LiveData<List<CategoryClass>>
        get() = categoryRepository.onlyExpenseCategories

    fun storeCurrentExpenseSelectChip(s: String) {
        currentSelectedExpenseCategory = s
    }

    fun storeCurrentIncomeSelectChip(s: String) {
        currentSelectedIncomeCategory = s
    }

    fun setTransactionTypeInViewModel(transaction: String) {
        transactionTypeFromViewModel = transaction
    }


    fun validateFormData(amount: String, note: String, mContext: Context?) {
        //to get,separate and validate data from both expense and income
        if (amount.isEmpty()) {
            EventBus.getDefault().post(EventMessage(4, "Amount can't be zero"))
        } else if (transactionTypeFromViewModel == Constants.EXPENSE) {
            category = currentSelectedExpenseCategory
            if (currentSelectedExpenseCategory.isEmpty()) {
                EventBus.getDefault().post(EventMessage(5, "Please select a category"))
            } else {
                //store the data
                Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show()
                storeFormDataInDatabase(
                    transactionTypeFromViewModel,
                    amount,
                    note,
                    category,
                    selectedDate
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
                    transactionTypeFromViewModel,
                    amount,
                    note,
                    category,
                    selectedDate
                )
            }
        }
    }


    private fun storeFormDataInDatabase(
        expense: String,
        amount: String,
        note: String,
        category: String?,
        dateWithTime: Long
    ) {
        // on below line we are creating
        // a variable for our modal class.
        val modal = TransactionClass()
        modal.amount = amount.trim { it <= ' ' }.replace(",", "").toLong()
        modal.type = expense.trim { it <= ' ' }
        modal.note = note.trim { it <= ' ' }
        modal.category = category!!.trim { it <= ' ' }
        modal.dateWithTime = dateWithTime
        val date = Instant.ofEpochMilli(dateWithTime)
            .atZone(ZoneId.systemDefault()) // default zone
            .toLocalDate()
        modal.date = date.toString().replace("-", "").toLong()


        //   Log.d("date in local date" ,d.toString());
        transactionRepository!!.insert(modal)
        EventBus.getDefault().post(EventMessage(6, "success"))

        //  liveData.postValue("something");
    }

    fun saveSelectedDate(date: Long) {
        this.selectedDate = date
    }

    val allTransactions: LiveData<List<TransactionClass>>
        get() = transactionRepository.allTransactions

    val allTransactionFlow = allTransactions.asFlow()

    fun validateCategoryData(categoryClass: CategoryClass) {
        if (categoryClass.categoryName!!.isEmpty()) {
            // textInputLayoutName.setError("Name field can't be empty");
            EventBus.getDefault().post(EventMessage(1, "Name field can't be empty"))
        } else if (categoryClass.categoryName!!.length < 3) {
            //textInputLayoutName.setError("Name must have at least 3 letters");
            EventBus.getDefault().post(EventMessage(12, "Name must have at least 3 letters"))
        } else if (categoryClass.categoryType!!.isEmpty()) {
            EventBus.getDefault().post(EventMessage(2, "Please select a category type"))
        } else {
            storeCategoryInDatabase(categoryClass)
        }
    }

    private fun storeCategoryInDatabase(categoryClass: CategoryClass) {
        if (categoryClass.id == 0L) {
            //new category insert
            categoryRepository.insert(categoryClass)
        } else {
            //existing category update
            categoryRepository.update(categoryClass)
        }

        EventBus.getDefault().post(EventMessage(3, "closing bottom sheet"))
    }

    fun fillCategoriesInDatabase(categoryClassesList: ArrayList<CategoryClass>) {
        for (categoryClass in categoryClassesList) {
            categoryRepository.insert(categoryClass)
        }
        EventBus.getDefault().post(EventMessage(9, "success"))
    }

    //to delete categories from database
    fun deleteCategoryFromDatabase(categoryClass: CategoryClass?) {
        categoryRepository.delete(categoryClass)
    }

    fun getTotalIncomeForRange(startDate: Long?, endDate: Long?): LiveData<TotalAmountClass> {
        return getTotalAmountByDateRangeAndCategoryType(startDate, endDate, Constants.INCOME)
    }

    fun getTotalExpenseForRange(startDate: Long?, endDate: Long?): LiveData<TotalAmountClass> {
        return getTotalAmountByDateRangeAndCategoryType(startDate, endDate, Constants.EXPENSE)
    }

    fun getTotalAmountByDateRangeAndCategoryType(
        startDate: Long?,
        endDate: Long?,
        categoryType: String?
    ): LiveData<TotalAmountClass> {
        return transactionRepository.getTotalAmountByDateRangeAndCategoryType(
            startDate,
            endDate,
            categoryType
        )
    }

    fun getListOfTotalAmountPerDayForRange(
        startDate: Long?,
        endDate: Long?
    ): LiveData<List<TotalExpenseIncomeClass>> {
        return transactionRepository.getListOfTotalAmountPerDayForRange(startDate, endDate)
    }

    fun deleteSingleTransaction(transactionClass: TransactionClass?) {
        transactionRepository.delete(transactionClass)
    }

    fun getAllTransactionsForDate(date: Long?): LiveData<List<TransactionClass>> {
        return transactionRepository.getAllTransactionsForDate(date)
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
}
