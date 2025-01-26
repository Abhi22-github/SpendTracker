package com.roaa.expensetracker.Composables.components

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.CustomFonts.numberFont
import com.roaa.expensetracker.Composables.utils.HarmonizedColorPalette
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.R


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

            Text(text = "₹24,045",
                style = MaterialTheme.typography.headlineMedium.copy(fontFamily = numberFont),
                modifier = Modifier.constrainAs(balanceText) {
                    top.linkTo(parent.top, margin = 24.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                })

            Text(text = "Amount",
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
                        text = "₹3,999",
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
                        text = "₹2000",
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
