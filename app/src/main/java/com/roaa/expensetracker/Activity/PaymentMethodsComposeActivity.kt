package com.roaa.expensetracker.Activity

import android.annotation.SuppressLint
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentMethodsComposeActivity() : ComponentActivity() {
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
                       // PaymentMethodScreen(Modifier, { sendUserBack() })
                    }
                }
            }
        }
    }

    private fun sendUserBack() {
        onBackPressedDispatcher.onBackPressed()
    }
}