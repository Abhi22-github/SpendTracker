package com.example.expensetracker.Composables.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.Composables.Screens.radioButtonColors
import com.example.expensetracker.R
import com.example.expensetracker.ViewModels.PreferencesViewModel


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetBottomSheet(
    sheetState: SheetState,
    bottomSheetDismissed: () -> Unit,
    amount: TextFieldValue,
    isBudgetSet: Boolean,
    preferenceViewModel: PreferencesViewModel = hiltViewModel(),
) {
    val modifier = Modifier.padding(16.dp, 0.dp)
    var amountText by remember { mutableStateOf(amount) }
    var shouldShowConfirmation by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    ModalBottomSheet(onDismissRequest = { bottomSheetDismissed() }, sheetState = sheetState) {
        BottomSheetContent(
            modifier,
            amountText,
            amountTextValueChange = {
                amountText = it
            },
            {
                if (isBudgetSet) {
                    shouldShowConfirmation = true
                } else {
                    saveDateToDevice(
                        preferenceViewModel,
                        amountText.text,
                        bottomSheetDismissed,
                        keyboardController,
                        focusManager
                    )
                }
            })
    }
    if (shouldShowConfirmation) {
        ConfirmationAlertDialog(
            { shouldShowConfirmation = false },
            {
                saveDateToDevice(
                    preferenceViewModel,
                    amountText.text,
                    bottomSheetDismissed,
                    keyboardController,
                    focusManager
                )
            },
            "Change Budget",
            "Are you sure, you want to change the current budget?",
            ImageVector.vectorResource(R.drawable.icon_expense)
        )
    }
}


@Composable
fun BottomSheetContent(
    modifier: Modifier,
    dailySpendLimit: TextFieldValue,
    amountTextValueChange: (TextFieldValue) -> Unit,
    saveDailySpendLimit: () -> Unit
) {

    Column(modifier.fillMaxWidth()) {
        Text(
            text = "Set up a budget",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = "Daily Budget", style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Enter your daily budget amount",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
        )
        TextField(
            value = dailySpendLimit,
            onValueChange = { newText ->
                val modifiedString = newText.text.filter { it in '0'..'9' || it == '.' }
                amountTextValueChange(TextFieldValue(modifiedString, newText.selection))
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 8.dp),
            placeholder = { Text("Amount", style = MaterialTheme.typography.bodyLarge) },
            leadingIcon = { Text("₹", style = MaterialTheme.typography.bodyLarge) },
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            // visualTransformation = NumberCommaTransformation()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { saveDailySpendLimit() },
        ) {
            Icon(painter = painterResource(R.drawable.icon_expense), contentDescription = null)
            Text(text = "Create Budget")
        }
    }
}





//class NumberCommaTransformation : VisualTransformation {
//    override fun filter(text: AnnotatedString): TransformedText {
//        return TransformedText(
//            text = AnnotatedString(text.text.toLongOrNull().formatWithComma()),
//            offsetMapping = object : OffsetMapping {
//                override fun originalToTransformed(offset: Int): Int {
//                    return text.text.toLongOrNull().formatWithComma().length
//                }
//
//                override fun transformedToOriginal(offset: Int): Int {
//                    return text.length
//                }
//            }
//        )
//    }
//}
//
//fun Long?.formatWithComma(): String =
//    NumberFormat.getNumberInstance(Locale.getDefault()).format(this ?: 0)

fun saveDateToDevice(
    preferenceViewModel: PreferencesViewModel,
    text: String,
    bottomSheetDismissed: () -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
) {
    preferenceViewModel.saveBudget(text.toFloat())
    preferenceViewModel.setBudgetState(true)
    focusManager.clearFocus()
    keyboardController?.hide()
    bottomSheetDismissed()

}


@Preview
@Composable
fun BottomSheetContentPreview() {
    ExpenseTrackerTheme {
        Surface {
            BottomSheetContent(Modifier, TextFieldValue(), {}, {})
        }
    }
}

@Preview
@Composable
fun SingleItemRadioButtonPreview() {
    SingleItemRadioButton("label", {}, "label")
}