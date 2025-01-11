package com.example.expensetracker.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.expensetracker.Composables.components.SpendsBudgetCard
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.example.expensetracker.databinding.ActivitySettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: AddActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //send user back to the previous activity on back icon pressed
        binding.toolbarExpenseCategoryToolbarSettingsActivity.setNavigationOnClickListener(View.OnClickListener { v: View? -> onBackPressedDispatcher.onBackPressed() })

        binding.composeView.setContent {
            SpendsBudgetCard(
                budget = 1000f,
                spend = 50f,
                viewModel = viewModel
            )
        }
        binding.button.setOnClickListener {
            viewModel.newTotal.postValue((Math.random()).toFloat())
        }
    }
}