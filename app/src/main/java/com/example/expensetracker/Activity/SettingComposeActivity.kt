package com.example.expensetracker.Activity

import android.annotation.SuppressLint
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.Composables.Screens.SettingsScreen
import com.example.expensetracker.ViewModels.TransactionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingComposeActivity : ComponentActivity() {
    val viewmodel: TransactionsViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                TRANSPARENT, TRANSPARENT
            )
        )
        setContent {
            ExpenseTrackerTheme {
                Scaffold {
                    Surface(modifier = Modifier.statusBarsPadding()) {
                        SettingsScreen({ sendUserBack() })
                    }
                }
            }
        }
    }

    fun sendUserBack() {
        onBackPressedDispatcher.onBackPressed()
    }
}
