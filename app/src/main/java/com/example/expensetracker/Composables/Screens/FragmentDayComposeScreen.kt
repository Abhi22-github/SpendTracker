package com.example.expensetracker.Composables.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.Composables.components.RestBudgetPill
import com.example.expensetracker.Composables.components.TransactionsListCompose
import com.example.expensetracker.Utilities.convertLocalDateToLong
import com.example.expensetracker.ViewModels.AnimationViewModel
import com.example.expensetracker.ViewModels.PreferencesViewModel
import com.example.expensetracker.ViewModels.TransactionsViewModel
import java.time.LocalDate

@Composable
fun FragmentDayScreen(
    showSingleDateTransactions: Boolean,
    date: LocalDate,
    animationViewModel: AnimationViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val longDate = convertLocalDateToLong(date)
    transactionsViewModel.getTotalExpenseForRange(
        longDate , longDate
    )
    val budget by preferencesViewModel.getBudgetValue.collectAsState(1f)
   // val percent by animationViewModel.newSpentPercentage.collectAsState()
    val newDailyBudget by transactionsViewModel.getTotalExpenseAmountForDateFlow.collectAsState()

    //LaunchedEffect(Unit)   {
//        val amountInString = String.format("%.2f", newDailyBudget.toFloat())
//        val percent = if (budget != 0f) {
//            newDailyBudget / budget
//        } else {
//            0f
//        }
//        scope.launch {
//            //animationViewModel.newSpentPercentage.emit(amount)
//            animationViewModel.newTotalString.emit("₹$amountInString")
//            animationViewModel.newSpentPercentage.emit(percent)
//            Log.d("Emmiting", amountInString)
//        }
        val amountInString = String.format("%.2f", newDailyBudget.toFloat())
        val percent = if (budget != 0f) {
            newDailyBudget / budget
        } else {
            0f
        }
        animationViewModel.method("₹$amountInString",percent)

  //  }


    Column {
        Row(modifier = Modifier.padding(12.dp, 16.dp)) {
            RestBudgetPill(date)
        }

//        scope.launch {
//            //animationViewModel.newSpentPercentage.emit(amount)
//            animationViewModel.newTotalString.emit("₹$amountInString")
//            animationViewModel.newSpentPercentage.emit(percent)
//            Log.d("Emmiting", amountInString)
//        }

//        Button(onClick = {
//            scope.launch {
//                val r = Math.random().toFloat()
//                animationViewModel.newSpentPercentage.emit(r)
//            }
//        }) {
//        }

        TransactionsListCompose(true, date)
    }
}


@Preview
@Composable
fun FragmentDayScreenPreview() {
    FragmentDayScreen(false, LocalDate.now())
}