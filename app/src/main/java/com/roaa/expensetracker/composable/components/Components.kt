package com.roaa.expensetracker.composable.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.R
import com.roaa.expensetracker.activity.LocalCurrency
import com.roaa.expensetracker.composable.CustomFonts.numberFont
import com.roaa.expensetracker.composable.utils.HarmonizedColorPalette
import com.roaa.expensetracker.composable.utils.IconState
import com.roaa.expensetracker.composable.utils.combineColors
import com.roaa.expensetracker.model.BankAccountsClass
import com.roaa.expensetracker.model.CategoryClass
import com.roaa.expensetracker.utilities.currentYear


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
    modifier: Modifier = Modifier,
    menuExpanded: Boolean,
    selectedCategory: CategoryClass,
    colorPallet: HarmonizedColorPalette,
    onDismiss: () -> Unit,
    itemList: List<CategoryClass>,
    selectedCategorySetter: (CategoryClass) -> Unit
) {
    DropdownMenu(
        modifier = modifier,
        expanded = menuExpanded,
        onDismissRequest = { onDismiss() },
        containerColor = colorPallet.surface,
        shape = RoundedCornerShape(24.dp),
    ) {
//        if (type == 1) {

        itemList.forEach { categoryClass ->
            val selectedModifier =
                if (selectedCategory.categoryId == categoryClass.categoryId) Modifier.background(
                    colorPallet.surfaceVariant
                ) else modifier
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
            }, modifier = selectedModifier)
        }

    }
}

@Composable
fun DropDownMenuForBankAccounts(
    menuExpanded: Boolean,
    selectedBankAccountsClass: BankAccountsClass,
    colorPallet: HarmonizedColorPalette,
    onDismiss: () -> Unit,
    itemList: List<BankAccountsClass>,
    selectedBankAccountSetter: (BankAccountsClass) -> Unit
) {
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { onDismiss() },
        containerColor = colorPallet.surface,
        shape = RoundedCornerShape(20.dp),
    ) {
//        if (type == 1) {
        itemList.forEach { bankAccountClass ->
            val selectedModifier =
                if (selectedBankAccountsClass.bankAccountId == bankAccountClass.bankAccountId) Modifier.background(
                    colorPallet.surfaceVariant
                ) else Modifier
            val selectedIcon =
                rememberAsyncImagePainter(IconState.fromNumber(bankAccountClass.cardIconNumber))
            DropdownMenuItem(
                text = { Text(text = bankAccountClass.bankName) },
                leadingIcon = {
                    Image(
                        painter = selectedIcon,
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                    )
                },
                onClick = {
                    selectedBankAccountSetter(bankAccountClass)
                    onDismiss()
                }, modifier = selectedModifier
            )
        }

    }
}

@Composable
fun DropDownMenuForCategory(
    menuExpanded: Boolean,
    selectedCategoryClass: CategoryClass,
    colorPallet: HarmonizedColorPalette,
    onDismiss: () -> Unit,
    itemList: List<CategoryClass>,
    selectedCategorySetter: (CategoryClass) -> Unit
) {
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { onDismiss() },
        containerColor = colorPallet.surface,
        shape = RoundedCornerShape(20.dp),
    ) {
//        if (type == 1) {
        itemList.forEach { categoryClass ->
            val selectedModifier =
                if (selectedCategoryClass.categoryId == categoryClass.categoryId) Modifier.background(
                    colorPallet.surfaceVariant
                ) else Modifier
            val selectedIcon =
                rememberAsyncImagePainter(IconState.fromNumber(categoryClass.categoryIconNumber))
            DropdownMenuItem(
                text = { Text(text = categoryClass.categoryName) },
                leadingIcon = {
                    Image(
                        painter = selectedIcon,
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                    )
                },
                onClick = {
                    selectedCategorySetter(categoryClass)
                    onDismiss()
                }, modifier = selectedModifier
            )
        }

    }
}

@Composable
fun SummaryCard(color: Color) {
    Card(
        shape = RoundedCornerShape(12.dp), modifier = Modifier
            .padding(16.dp, 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                //  onSingleItemClick(item)
            },
        colors = CardDefaults.cardColors(
            containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                color,
                angle = 0.1f,
            )
        )
    ) {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (balanceText, balanceLabel, expense, income, moreIcon, backgroundImage1, backgroundImage2, progress) = createRefs()

            Text(
                text = "${LocalCurrency.current.currencySymbol}24,045",
                style = MaterialTheme.typography.headlineMedium.copy(fontFamily = numberFont),
                modifier = Modifier.constrainAs(balanceText) {
                    top.linkTo(parent.top, margin = 24.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                })

            Text(
                text = "Amount",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
                modifier = Modifier.constrainAs(balanceLabel) {
                    top.linkTo(balanceText.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                })

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(expense) {
                    top.linkTo(progress.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                    bottom.linkTo(parent.bottom, margin = 24.dp)
                },
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .size(48.dp)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        val image = rememberAsyncImagePainter(R.drawable.icon_expense)
                        Image(
                            painter = image,
                            contentDescription = "Test Image",
                            modifier = Modifier.size(48.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)

                ) {
                    Text(
                        text = "${LocalCurrency.current.currencySymbol}3,999",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f),
                        style = typography.titleMedium.copy(
                            fontFamily = numberFont,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                    if (true) {
                        Text(
                            text = "Expense",
                            style = typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        )
                    }
                }


            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(income) {
                    top.linkTo(progress.bottom, margin = 24.dp)
                    end.linkTo(parent.end, margin = 24.dp)
                    bottom.linkTo(parent.bottom, margin = 24.dp)
                    width = Dimension.wrapContent
                },
                horizontalArrangement = Arrangement.Center
            ) {

                Surface(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .size(48.dp)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        val image = rememberAsyncImagePainter(R.drawable.icon_income)
                        Image(
                            painter = image,
                            contentDescription = "Test Image",
                            modifier = Modifier.size(48.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)

                ) {
                    Text(
                        text = "${LocalCurrency.current.currencySymbol}2000",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f),
                        style = typography.titleMedium.copy(
                            fontFamily = numberFont,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                    if (true) {
                        Text(
                            text = "Income",
                            style = typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        )
                    }
                }


            }

            val image = rememberAsyncImagePainter(R.drawable.shape_soft_star_1)
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(144.dp)
                    .constrainAs(backgroundImage1) {
                        top.linkTo(parent.top, margin = -30.dp)
                        start.linkTo(parent.start, margin = -30.dp)
                    }, alpha = 0.1f, colorFilter = ColorFilter.tint(color)
            )
            LinearProgressIndicator(
                progress = { Math.random().toFloat() },
                modifier = Modifier
                    .height(10.dp)
                    .constrainAs(progress) {
                        top.linkTo(balanceLabel.bottom, 20.dp)
                        start.linkTo(parent.start, 24.dp)
                        end.linkTo(parent.end, 24.dp)
                        width = Dimension.fillToConstraints
                    },
                color = color.copy(alpha = 0.30f),
                trackColor = color.copy(alpha = 0.10f),
                strokeCap = StrokeCap.Round,
                gapSize = 0.dp,
                drawStopIndicator = {
                    drawStopIndicator(
                        drawScope = this,
                        stopSize = ProgressIndicatorDefaults.CircularStrokeWidth,
                        color = color,
                        strokeCap = StrokeCap.Round
                    )
                }
            )
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(144.dp)
                    .constrainAs(backgroundImage2) {
                        end.linkTo(parent.end, margin = -30.dp)
                        bottom.linkTo(parent.bottom, margin = -30.dp)
                    }, alpha = 0.1f, colorFilter = ColorFilter.tint(color)
            )

            IconButton(
                onClick = { }, modifier = Modifier
                    .constrainAs(moreIcon) {
                        top.linkTo(parent.top, 18.dp)
                        end.linkTo(parent.end, 18.dp)
                    }
            ) {
                Icon(
                    Icons.Filled.MoreVert, contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun DropDownBankAccountOption(
    menuExpanded: Boolean,
    colorPallet: HarmonizedColorPalette,
    onDismiss: () -> Unit,
    onPrimaryClicked: () -> Unit,
    editClicked: () -> Unit,
    deleteClicked: () -> Unit
) {
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { onDismiss() },
        containerColor = colorPallet.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
    ) {
        val primaryIcon =
            rememberAsyncImagePainter(R.drawable.round_star)
        val editIcon =
            rememberAsyncImagePainter(R.drawable.round_edit)
        val deleteIcon =
            rememberAsyncImagePainter(R.drawable.round_delete_outline_24)
        DropdownMenuItem(text = { Text(text = "Set As Primary") }, leadingIcon = {
            Image(
                painter = primaryIcon,
                contentDescription = "edit",
                modifier = Modifier.size(24.dp),
            )
        }, onClick = {
            onPrimaryClicked()
            onDismiss()
        })
        DropdownMenuItem(text = { Text(text = "Edit") }, leadingIcon = {
            Image(
                painter = editIcon,
                contentDescription = "edit",
                modifier = Modifier.size(24.dp),
            )
        }, onClick = {
            editClicked()
            onDismiss()
        })
        DropdownMenuItem(text = { Text(text = "Delete") }, leadingIcon = {
            Image(
                painter = deleteIcon,
                contentDescription = "delete",
                modifier = Modifier.size(24.dp),
            )
        }, onClick = {
            deleteClicked()
            onDismiss()
        })


    }
}


@Composable
fun CircularProgress(modifier: Modifier = Modifier) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ChipsForFilter(
    index: Int, selectedIndexForFilterChip: Int, text: String, selectChip: (Int) -> Unit
) {
    val temp = if (text.split(",").get(1) == currentYear) text.split(",")[0] else text
    FilterChip(
        onClick = { selectChip(index) },
        label = {
            Text(text = temp)
        },
        selected = index == selectedIndexForFilterChip,
        leadingIcon = if (index == selectedIndexForFilterChip) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.1.dp, MaterialTheme.colorScheme.outline),
        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh))
}

@Composable
fun BankChips(
    bankAccountsClass: BankAccountsClass,
    isSelected: Boolean,
    setChipAsSelected: (BankAccountsClass) -> Unit
) {
    FilterChip(
        onClick = { setChipAsSelected(bankAccountsClass) },
        label = {
            Text(
                text = bankAccountsClass.bankName,
                modifier = Modifier.padding(vertical = 8.dp),
                style = typography.bodyMedium
            )
        },
        selected = isSelected,
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        shape = RoundedCornerShape(25.dp),
        border = if (isSelected) BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        ) else BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
        colors = FilterChipDefaults.filterChipColors())
}

@Composable
fun CategoryTypeChips(
    index: Int,
    selectedIndex: Int,
    text: String,
    selectChip: (Int) -> Unit
) {
    FilterChip(
        onClick = { selectChip(index) },
        label = {
            Text(
                text = text,
                modifier = Modifier.padding(vertical = 8.dp),
                style = typography.bodyMedium
            )
        },
        selected = index == selectedIndex,
        leadingIcon = if (index == selectedIndex) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        shape = RoundedCornerShape(25.dp),
        border = if (index == selectedIndex) BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        ) else BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
        colors = FilterChipDefaults.filterChipColors())
}

@Composable
fun CategoryChips(
    categoryClass: CategoryClass,
    isSelected: Boolean,
    setChipAsSelected: (CategoryClass) -> Unit
) {
    FilterChip(
        onClick = { setChipAsSelected(categoryClass) },
        label = {
            Text(
                text = categoryClass.categoryName,
                modifier = Modifier.padding(vertical = 8.dp),
                style = typography.bodyMedium
            )
        },
        selected = isSelected,
        leadingIcon =
            {
                Image(
                    painter = painterResource(IconState.fromNumber(categoryClass.categoryIconNumber)),
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            },
        shape = RoundedCornerShape(25.dp),
        border = if (isSelected) BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        ) else BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
        colors = FilterChipDefaults.filterChipColors()
    )
}


@Composable
fun ViewItemBottomRow(
    modifier: Modifier = Modifier,
    editButtonClicked: () -> Unit,
    deleteButtonClicked: () -> Unit
) {
    Row(
        Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 8.dp),
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextButton(onClick = { editButtonClicked() }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "edit",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text("Edit")
                        }
                    }
                    Row(
                        Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        TextButton(
                            onClick = {
                                deleteButtonClicked()
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),

                            ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "delete",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text("Delete")
                        }
                    }
                }

            }
        }
    }

}