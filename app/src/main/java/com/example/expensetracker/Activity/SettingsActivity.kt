package com.example.expensetracker.Activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.expensetracker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
       setContentView(binding.root)

        //send user back to the previous activity on back icon pressed
        binding.toolbarExpenseCategoryToolbarSettingsActivity.setNavigationOnClickListener(View.OnClickListener { v: View? -> onBackPressedDispatcher.onBackPressed() })
    }
}