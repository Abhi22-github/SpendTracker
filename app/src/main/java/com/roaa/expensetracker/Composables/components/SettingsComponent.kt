package com.roaa.expensetracker.Composables.components


import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.roaa.expensetracker.Composables.Screens.radioButtonColors


@Composable
fun SingleItemRadioButton(
    labelText: String,
    selectedItem: (String) -> Unit,
    themeSelected: String,
) {
    Row() {
        RadioButton(
            selected = (themeSelected == labelText),
            onClick = { selectedItem(labelText) },
            colors = radioButtonColors
        )
        Text(
            text = labelText.lowercase().replaceFirstChar { it -> it.uppercase() },
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@Preview
@Composable
fun SingleItemRadioButtonPreview() {
    SingleItemRadioButton("label", {}, "label")
}