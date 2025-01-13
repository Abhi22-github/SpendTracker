package com.example.expensetracker.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.Composables.Screens.SettingsScreen
import com.example.expensetracker.ViewModels.AddActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingComposeActivity : ComponentActivity() {
    val viewmodel:AddActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerTheme {
                Surface {
                    SettingsScreen({ sendUserBack() })
                }
            }
        }
    }

    fun sendUserBack() {
        onBackPressedDispatcher.onBackPressed()
    }
}
