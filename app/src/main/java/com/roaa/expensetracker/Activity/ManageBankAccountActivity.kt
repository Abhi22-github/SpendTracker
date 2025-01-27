package com.roaa.expensetracker.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import com.roaa.expensetracker.Composables.components.BottomSheetContentAddItemTest
import com.roaa.expensetracker.Composables.components.SpendsBudgetCard
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.databinding.ActivitySettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageBankAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: TransactionsViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)


        //send user back to the previous activity on back icon pressed
        binding.toolbarExpenseCategoryToolbarSettingsActivity.setNavigationOnClickListener(View.OnClickListener { v: View? -> onBackPressedDispatcher.onBackPressed() })

        binding.composeView.setContent {
            ExpenseTrackerTheme {
                var state by remember { mutableStateOf(false) }
                SpendsBudgetCard(
                    budget = 1000f,
                    spend = 50f,
                )
                Button(onClick = { state = !state }) { }
                val bottomSheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                if (state)
                    BottomSheetContentAddItemTest(bottomSheet, { state = !state })
            }
        }
        binding.button.setOnClickListener {
            viewModel.newTotal.postValue((Math.random()).toFloat())
        }
    }
}