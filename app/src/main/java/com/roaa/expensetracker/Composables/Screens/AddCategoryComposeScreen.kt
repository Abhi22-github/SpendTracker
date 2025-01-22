package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.Composables.secondaryAlpha

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddCategory(navController: NavController) {
    var categoryName by remember { mutableStateOf("") }
    ExpenseTrackerTheme {
        Scaffold(topBar = {
            TopBar("Add Category") {
                navController.popBackStack()
            }
        }) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Category Name", style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "give some category name",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha)
                )
                TextField(
                    value = categoryName,
                    onValueChange = { newText ->
                        categoryName = newText
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 8.dp),
                    placeholder = {
                        Text(
                            "Category Name",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha)
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
                /*
                * Icons Box
                */
                Card() { }


                /*Button*/
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { },
                ) {
                    Text(text = "Save Category")
                }


            }
        }
    }
}
