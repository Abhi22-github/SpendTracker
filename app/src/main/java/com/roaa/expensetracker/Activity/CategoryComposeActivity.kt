package com.roaa.expensetracker.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import com.roaa.expensetracker.Composables.Navigation.SetupNavigationGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ExpenseTrackerTheme {
                Scaffold {
                    Surface(modifier = Modifier.statusBarsPadding()) {
//                        CategoryScreen(
//                            navController = navController,
//                            sendUserBack = { sendUserBack() },
//                        )
                        SetupNavigationGraph()
                    }
                }
            }
        }
    }



}
