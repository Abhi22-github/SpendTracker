package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cable
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.CustomFonts.numberFont
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.components.TopBarForTransactionDetailsScreen
import com.roaa.expensetracker.Composables.components.bottomSheetStartEndPadding
import com.roaa.expensetracker.Composables.components.bottomSheetTopBottomPadding
import com.roaa.expensetracker.Composables.components.spaceHeightInDetail
import com.roaa.expensetracker.Composables.components.valueArrangement
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.secondaryAlpha
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Model.emptyBank
import com.roaa.expensetracker.Model.emptyCategoryClass
import com.roaa.expensetracker.Model.emptyTransactionClass
import com.roaa.expensetracker.Model.firstSampleClass
import com.roaa.expensetracker.R
import com.roaa.expensetracker.ViewModels.CategoryViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionDetailsScreen(
    modifier: Modifier = Modifier,
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    amount: Float,
    categoryName1: String,
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    Scaffold(topBar = { TopBarForTransactionDetailsScreen("", false, {}, {}) }) {
        Surface {
            val scroll = rememberScrollState()
            val orangePalette = toPalette(orange)
            val labelAndValueStyle = typography.bodyMedium
            val singleTransaction = TransactionWithDetails(
                emptyTransactionClass, emptyCategoryClass,
                emptyBank
            )

            val gradient =
                Brush.verticalGradient(
                    listOf(
                        orangePalette.main.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scroll)
                    .background(gradient)
            ) {

                Spacer(Modifier.height(128.dp))
                Card(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.size(128.dp),
                    colors = CardDefaults.cardColors(containerColor = orangePalette.main.copy(alpha = 0.3f))
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(R.drawable.expense_icon_new),
                            modifier = Modifier.size(64.dp),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(orangePalette.main)
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "₹ $amount",
                    style = MaterialTheme.typography.displayMedium.copy(fontFamily = numberFont),
                    color = orangePalette.main
                )
//        Text(text = "Food & Drink", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(0.dp))
                Text(
                    text = "Sharma World and Sun cafe Coffee",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
                Spacer(Modifier.height(24.dp))

                var categoryMenuExpanded by remember { mutableStateOf(false) }
                val categoryList by categoryViewModel.allCategoryList.collectAsState()
                var selectedCategory by remember {
                    mutableStateOf(
                        firstSampleClass.apply {
                            categoryName = "Food & Expense"
                            categoryIconNumber = 1
                        }
                    )
                }

                Box(
                    modifier = modifier.wrapContentWidth()
                ) {
                    Button(
                        modifier = Modifier.padding(end = 0.dp),
                        onClick = { categoryMenuExpanded = !categoryMenuExpanded },
                        colors = ButtonColors(
                            containerColor = orangePalette.container.copy(alpha = 0.3f),
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(
                            start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp
                        )
                    ) {
                        val image =
                            rememberAsyncImagePainter(IconState.fromNumber(selectedCategory.categoryIconNumber))
                        Image(
                            painter = image,
                            contentDescription = "Test Image",
                            modifier = Modifier.size(24.dp),
                        )

                        Text(
                            text = selectedCategory.categoryName,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(start = 8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
//                        Icon(
//                            Icons.Filled.KeyboardArrowDown,
//                            "backIcon",
//                            modifier = Modifier
//                        )
                    }
//                    DropDownMenu(
//                        Modifier,
//                        categoryMenuExpanded,
//                        orangePalette,
//                        onDismiss = { categoryMenuExpanded = false },
//                        categoryList,
//                        selectedCategorySetter = {
//                            selectedCategory = it
////                    scope.launch {
////                        uiViewModel.errorStatusInAddBottomSheet.emit(false)
////                    }
//                        },
//                    )

                }

                if (false)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(
                            5.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(top = 12.dp)

                    ) {
                        TagChip("Morning")
                        TagChip("Akurdi")
                        TagChip("Money")
                        TagChip("Hello Tag")
                        TagChip("Paid by Hrishi")
                        TagChip("Pune")
                    }
                Spacer(Modifier.height(48.dp))
                Spacer(Modifier.height(20.dp))
                Row(
                    Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(16.dp),
                    ) {
                        Spacer(Modifier.width(12.dp))
                        Column {
                            ValueLabelList(
                                Modifier,
                                labelAndValueStyle,
                                "Type",
                                "Expense",
                                23,
                                Icons.Outlined.Cable
                            )
                            Spacer(Modifier.height(spaceHeightInDetail))
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceContainer, thickness = 1.dp
                            )
                            Spacer(Modifier.height(spaceHeightInDetail))
                            ValueLabelList(
                                Modifier,
                                labelAndValueStyle,
                                "Category",
                                "Food & Expense",
                                23,
                                Icons.Outlined.Category
                            )
                            Spacer(Modifier.height(spaceHeightInDetail))
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceContainer, thickness = 1.dp
                            )
                            Spacer(Modifier.height(spaceHeightInDetail))
                            ValueLabelList(
                                Modifier,
                                labelAndValueStyle,
                                "Date",
                                "12th Aug 2024",
                                23,
                                Icons.Outlined.DateRange
                            )
                            Spacer(Modifier.height(spaceHeightInDetail))
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceContainer, thickness = 1.dp
                            )
                            Spacer(Modifier.height(spaceHeightInDetail))
                            ValueLabelList(
                                Modifier,
                                labelAndValueStyle,
                                "Payment Method",
                                "HDFC Bank",
                                23,
                                Icons.Outlined.Payment
                            )
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                if (false)
                    Row(
                        Modifier.padding(bottomSheetStartEndPadding, bottomSheetTopBottomPadding)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(16.dp),
                        ) {
                            Spacer(Modifier.width(12.dp))
                            Column {
                                ValueLabelList(
                                    Modifier,
                                    labelAndValueStyle,
                                    "Type",
                                    "Expense",
                                    23,
                                    Icons.Outlined.Cable
                                )
                                Spacer(Modifier.height(spaceHeightInDetail))
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.surfaceContainer,
                                    thickness = 1.dp
                                )
                                Spacer(Modifier.height(spaceHeightInDetail))
                                ValueLabelList(
                                    Modifier,
                                    labelAndValueStyle,
                                    "Category",
                                    "Food & Expense",
                                    23,
                                    Icons.Outlined.Category
                                )
                                Spacer(Modifier.height(spaceHeightInDetail))
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.surfaceContainer,
                                    thickness = 1.dp
                                )
                                Spacer(Modifier.height(spaceHeightInDetail))
                                ValueLabelList(
                                    Modifier,
                                    labelAndValueStyle,
                                    "Date",
                                    "12th Aug 2024",
                                    23,
                                    Icons.Outlined.DateRange
                                )
                            }
                        }
                    }

//
//                Box(
//                    Modifier
//                        .wrapContentHeight()
//                        .fillMaxWidth(), contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        Modifier
//                            .fillMaxWidth()
//                            .padding(12.dp)
//                    ) {
//
////                        Row(
////                            Modifier
////                                .fillMaxWidth()
////
////                        ) {
////                            SingleInfoBoxForTransactions(
////                                Modifier.weight(1f),
////                                "Budget Category",
////                                "Food & Expenses",
////                                Color.Blue,
////                                R.drawable.ic_category_1
////                            )
////                            Spacer(Modifier.width(12.dp))
////                            SingleInfoBoxForTransactions(
////                                Modifier.weight(1f),
////                                "Transaction Type",
////                                "Expense",
////                                orange,
////                                R.drawable.icon_expense
////                            )
////                        }
//                        Spacer(Modifier.height(12.dp))
//                        Row(
//                            Modifier
//                                .fillMaxWidth()
//                        ) {
//                            SingleInfoBoxForTransactions(
//                                Modifier.weight(1f),
//                                "Transaction Date",
//                                "30 July 2025",
//                                Color.Red,
//                                R.drawable.icon_round_calender
//                            )
//                            Spacer(Modifier.width(12.dp))
//                            SingleInfoBoxForTransactions(
//                                Modifier.weight(1f),
//                                "Paid By",
//                                "HDFC Bank",
//                                greenColor,
//                                R.drawable.ic_category_25
//                            )
//                        }
//                    }
//
//                }

            }
        }
    }
}

@Composable
fun ValueLabelList(
    modifier: Modifier = Modifier,
    labelAndValueStyle: TextStyle,
    labelName: String,
    labelValue: String,
    iconNumber: Int,
    image: ImageVector
) {
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
//            val image = rememberAsyncImagePainter(
//                IconState.fromNumber(iconNumber)
//            )
            Icon(
                image,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha)
            )
//            Image(
//                painter = image,
//                contentDescription = "Test Image",
//                modifier = Modifier.size(24.dp),
//            )
            Text(
                text = labelName,
                modifier = Modifier.padding(start = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = secondaryAlpha),
                style = labelAndValueStyle
            )
        }
        Row(
            Modifier.weight(1f),
            horizontalArrangement = valueArrangement,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = labelValue,
                modifier = Modifier.padding(start = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = labelAndValueStyle
            )
        }
    }
}

@Composable
fun SingleInfoBoxForTransactions(
    modifier: Modifier,
    label: String,
    value: String,
    color: Color,
    icon: Int
) {
    val paletteColor = toPalette(color)
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.3f
            )
        ),
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(5.dp, 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(48.dp),
                colors = CardDefaults.cardColors(containerColor = paletteColor.main.copy(alpha = 0.1f))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(icon),
                        modifier = Modifier.size(24.dp),
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = label, style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(0.38f)
            )
            Spacer(Modifier.height(0.dp))
            Text(
                text = value, style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

}

@Composable
fun TagChip(text: String) {
    SuggestionChip(
        onClick = { Log.d("Suggestion chip", "hello world") },
        label = { Text(text) },
        colors = SuggestionChipDefaults.suggestionChipColors(
            labelColor = MaterialTheme.colorScheme.onSurface.copy(
                0.6f
            )
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(15.dp)
    )
}