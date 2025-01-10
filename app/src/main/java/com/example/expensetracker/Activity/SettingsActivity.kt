package com.example.expensetracker.Activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.Composables.components.SpendsBudgetCard
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.example.expensetracker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: AddActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get<AddActivityViewModel>(
            AddActivityViewModel::class.java
        )
        viewModel.initializeDatabaseRepository(application)

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