package com.roaa.expensetracker.viewModels

import androidx.lifecycle.ViewModel
import com.roaa.expensetracker.database.relations.TransactionWithDetails
import com.roaa.expensetracker.utilities.ErrorManager
import com.roaa.expensetracker.utilities.currentMonth
import com.roaa.expensetracker.utilities.utilityModalClass.defaultBank
import com.roaa.expensetracker.utilities.utilityModalClass.defaultCategoryClass
import com.roaa.expensetracker.utilities.utilityModalClass.emptyBank
import com.roaa.expensetracker.utilities.utilityModalClass.emptyCategoryClass
import com.roaa.expensetracker.utilities.utilityModalClass.emptyTransactionClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class UiViewModel @Inject constructor() : ViewModel() {
    //when user clicks on add transaction from specific bank or category
    var addCategorySpecificOrBankSpecificTransaction = MutableStateFlow<Boolean>(false)
    var addSpecificCategoryForTransaction = MutableStateFlow(emptyCategoryClass)
    var addSpecificBankForTransaction = MutableStateFlow(emptyBank)
    var errorStatusInAddBottomSheet = MutableStateFlow<Boolean>(false)
    var errorStatusInAddCategory = MutableStateFlow<Boolean>(false)
    var errorStatusInBankAccountAdd = MutableStateFlow<Boolean>(false)
    var errorStatusInBudgetAdd = MutableStateFlow<Boolean>(false)
    var errorStatusInSetupBudget = MutableStateFlow<Boolean>(false)
    var errorStatusInStatisticsFilter = MutableStateFlow(false)
    //var errorStatusMessage = MutableStateFlow<String>("")

    fun setErrorMessage(message: String) {
        ErrorManager.errorMessage.value = message
    }


    var selectedIconFromBottomSheet = MutableStateFlow<Int>(99)
    var addCategoryBackPressed = MutableStateFlow(false)


    //data Related
    var transactionDetailsWithViewModelFlow = MutableStateFlow(
        TransactionWithDetails(
            emptyTransactionClass, defaultCategoryClass, defaultBank
        )
    )

    //

    var showMonthFilterChips = MutableStateFlow<Boolean>(false)
    var selectedMonth = MutableStateFlow(currentMonth)

}