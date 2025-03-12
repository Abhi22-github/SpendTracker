package com.roaa.expensetracker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.database.TransactionRepository
import com.roaa.expensetracker.utilities.preferenceManger.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AnimationViewModel @Inject constructor(
    val repository: TransactionRepository,
    val preferenceManager: PreferenceManager
) :
    ViewModel() {
    val newSpentPercentage: MutableStateFlow<Float>
    val oldPercentage: MutableStateFlow<Float>
    val newTotalString: MutableStateFlow<String>
    val state: MutableStateFlow<DaileBudgetState>


    init {
        newSpentPercentage = MutableStateFlow(1f)
        oldPercentage = MutableStateFlow(1f)
        newTotalString = MutableStateFlow("₹100")
        state = MutableStateFlow(DaileBudgetState.NORMAL)
    }

    fun method(s: String, percent: Float) {
        // transactionsViewModel.get
        viewModelScope.launch {
            newTotalString.emit(s)
            newSpentPercentage.emit(1 - percent)
        }
        viewModelScope.launch {
            if (percent < 0.3f) {
                state.emit(DaileBudgetState.NORMAL)
            } else if (percent >= 0.3f && percent <= 0.8f) {
                state.emit(DaileBudgetState.MIDDLE)
            } else if (percent > 0.8f && percent <= 1.00) {
                state.emit(DaileBudgetState.END)
            } else {
                state.emit(DaileBudgetState.OVERSPEND)
            }
        }
    }


}

