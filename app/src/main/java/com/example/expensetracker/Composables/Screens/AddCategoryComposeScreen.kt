package com.example.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.expensetracker.Composables.ExpenseTrackerTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddCategory(navController: NavHostController) {
    ExpenseTrackerTheme {
        Scaffold {
            Text(text = "Add category")

        }
    }
}