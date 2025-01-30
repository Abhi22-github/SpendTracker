package com.roaa.expensetracker.Composables.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.components.RestBudgetPill
import com.roaa.expensetracker.Composables.components.TransactionsListCompose
import com.roaa.expensetracker.Utilities.convertLocalDateToLong
import com.roaa.expensetracker.ViewModels.AnimationViewModel
import com.roaa.expensetracker.ViewModels.PreferencesViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import java.time.LocalDate

@Composable
fun FragmentDayScreen(
    navigationManager: NavigationManager,
    showSingleDateTransactions: Boolean,
    date: LocalDate,
    animationViewModel: AnimationViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {

    val longDate = convertLocalDateToLong(date)
    transactionsViewModel.getTotalExpenseForRange(
        longDate, longDate
    )
    val budget by preferencesViewModel.getBudgetValue.collectAsState(1f)
    val newDailyBudget by transactionsViewModel.getTotalExpenseAmountForDateFlow.collectAsState()

    val amountInString = String.format("%.2f", newDailyBudget.toFloat())
    val percent = if (budget != 0f) {
        newDailyBudget / budget
    } else {
        0f
    }
    animationViewModel.method("₹$amountInString", percent)

    Column {
        Row(modifier = Modifier.padding(12.dp, 16.dp)) {
            RestBudgetPill(date)
        }

        TransactionsListCompose(navigationManager, Modifier, true, date)
    }
}


@Preview
@Composable
fun FragmentDayScreenPreview() {
    // FragmentDayScreen(false, LocalDate.now())
}