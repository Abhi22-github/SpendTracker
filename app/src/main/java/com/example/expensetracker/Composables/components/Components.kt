package com.example.expensetracker.Composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.expensetracker.Composables.utils.HarmonizedColorPalette
import com.example.expensetracker.Composables.utils.IconState
import com.example.expensetracker.Model.CategoryClass


@Composable
fun DateInputChip(
    text: String,
    onDismiss: () -> Unit,
) {
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return

    InputChip(
        onClick = {
            onDismiss()
            enabled = !enabled
        },
        label = { Text(text) },
        selected = enabled,
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = "Localized description",
            )
        },
        colors = InputChipDefaults.inputChipColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    )
}

//@Preview
//@Composable
//fun DateChipPreview() {
//    DateInputChip("Hello") { }
//}


//type 1 -> CategoryList
//type 2 -> expense/Income
@Composable
fun DropDownMenu(
    menuExpanded: Boolean,
    colorPallet: HarmonizedColorPalette,
    onDismiss: () -> Unit,
    itemList: List<CategoryClass>,
    selectedCategorySetter: (CategoryClass) -> Unit
) {
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { onDismiss() },
        containerColor = colorPallet.surfaceVariant,
        shape = RoundedCornerShape(24.dp),
    ) {
//        if (type == 1) {
        itemList.forEach { categoryClass ->
            val selectedIcon =
                rememberAsyncImagePainter(IconState.fromNumber(categoryClass.categoryIconNumber))
            DropdownMenuItem(text = { Text(text = categoryClass.categoryName) }, leadingIcon = {
                Image(
                    painter = selectedIcon,
                    contentDescription = categoryClass.categoryName,
                    modifier = Modifier.size(24.dp),
                )
            }, onClick = {
                selectedCategorySetter(categoryClass)
                onDismiss()
            })
        }
        //     }
//        if (type == 2) {
//            typeList?.forEach { transactionTypeClass ->
//                val selectedIcon =
//                    rememberAsyncImagePainter(IconStateForType.fromNumber(transactionTypeClass.iconNumber))
//                DropdownMenuItem(text = { Text(text = transactionTypeClass.type) }, leadingIcon = {
//                    Image(
//                        painter = selectedIcon,
//                        contentDescription = "",
//                        modifier = Modifier.size(24.dp),
//                    )
//                }, onClick = {
//                    selectedTypeSetter(transactionTypeClass)
//                    selectedCategorySetter(CategoryClass(-1, "Select", 1, 99, EXPENSE))
//                    onDismiss()
//                })
//            }
//        }

    }
}

