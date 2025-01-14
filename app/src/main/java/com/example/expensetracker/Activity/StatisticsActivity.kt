package com.example.expensetracker.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.Composables.components.RestBudgetPill
import com.example.expensetracker.Composables.components.SpendsBudgetCard
import com.example.expensetracker.Composables.components.TransactionsListCompose
import com.example.expensetracker.ViewModels.TransactionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class StatisticsActivity : ComponentActivity() {
    private val viewModel: TransactionsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerTheme {
                val percent by viewModel.newTotal.observeAsState()
                Column {
                    SpendsBudgetCard(
                        modifier = Modifier.height(100.dp),
                        budget = 1000f,
                        spend = 50f,
                    )
                    Button(onClick = { incrementSpend() }) {
                    }
                    Row {
                        RestBudgetPill(date = LocalDate.now(), transactionsViewModel = viewModel)
                    }
                    TransactionsListCompose(false, LocalDate.now(),viewModel = viewModel)
                }

            }
        }
    }

    fun incrementSpend() {
        viewModel.newTotal.postValue((Math.random()).toFloat())
    }
}