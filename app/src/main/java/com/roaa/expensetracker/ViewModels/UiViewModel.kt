package com.roaa.expensetracker.ViewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class UiViewModel @Inject constructor() : ViewModel() {
    var transactionDetailBottomSheetValue = MutableStateFlow<Boolean>(false)
    var errorStatusInAddBottomSheet = MutableStateFlow<Boolean>(false)
    var errorStatusInAddCategory = MutableStateFlow<Boolean>(false)
    var errorStatusMessage = MutableStateFlow<String>("")

    var selectedIconFromBottomSheet = MutableStateFlow<Int>(99)
    var addCategoryBackPressed = MutableStateFlow(false)

    var paymentMethodBottomSheetStatus = MutableStateFlow(false)

    var showDimTextOnLivePaymentCard = MutableStateFlow(true)
    var liveBankAmount = MutableStateFlow("")
    var liveBankNumber = MutableStateFlow("")
    var liveBankName = MutableStateFlow("")

}