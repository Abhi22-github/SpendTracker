package com.example.expensetracker.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.Composables.components.RestBudgetPill
import com.example.expensetracker.Composables.components.SpendsBudgetCard
import com.example.expensetracker.ViewModels.AddActivityViewModel

class StatisticsActivity : ComponentActivity() {
    lateinit var viewModel: AddActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get<AddActivityViewModel>(
            AddActivityViewModel::class.java
        )
        viewModel.initializeDatabaseRepository(application)
        setContent {
            val percent by viewModel.newTotal.observeAsState()
            Column {
                SpendsBudgetCard(
                    modifier = Modifier.height(100.dp),
                    budget = 1000f,
                    spend = 50f,
                    viewModel = viewModel
                )
                Button(onClick = { incrementSpend() }) {
                }
                Row {
                    RestBudgetPill(viewModel = viewModel)
                }
             
            }
        }
    }
    fun incrementSpend(){
        viewModel.newTotal.postValue((Math.random()).toFloat())
    }
}