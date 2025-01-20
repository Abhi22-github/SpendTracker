package com.example.expensetracker.ViewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class UiViewModel @Inject constructor() : ViewModel() {
    var transactionDetailBottomSheetValue = MutableStateFlow<Boolean>(false)
    var errorStatusInAddBottomSheet = MutableStateFlow<Boolean>(false)
    var errorStatusMessage = MutableStateFlow<String>("")
}