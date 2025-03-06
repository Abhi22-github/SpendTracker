package com.roaa.expensetracker.ViewModels

import androidx.lifecycle.ViewModel
import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBank
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyCategoryClass
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyTransactionClass
import com.roaa.expensetracker.Utilities.currentMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class UiViewModel @Inject constructor() : ViewModel() {
    var transactionDetailBottomSheetValue = MutableStateFlow<Boolean>(false)
    var errorStatusInAddBottomSheet = MutableStateFlow<Boolean>(false)
    var errorStatusInAddCategory = MutableStateFlow<Boolean>(false)
    var errorStatusInBankAccountAdd = MutableStateFlow<Boolean>(false)
    var errorStatusInBudgetAdd = MutableStateFlow<Boolean>(false)
    var errorStatusInSetupBudget = MutableStateFlow<Boolean>(false)
    var errorStatusInStatisticsFilter = MutableStateFlow(false)
    var errorStatusMessage = MutableStateFlow<String>("")


    var selectedIconFromBottomSheet = MutableStateFlow<Int>(99)
    var addCategoryBackPressed = MutableStateFlow(false)


    //data Related
    var transactionDetailsWithViewModelFlow = MutableStateFlow(
        TransactionWithDetails(
            emptyTransactionClass, emptyCategoryClass, emptyBank
        )
    )

    //

    var showMonthFilterChips = MutableStateFlow<Boolean>(false)
    var selectedMonth = MutableStateFlow(currentMonth)

}