package com.example.expensetracker.Composables.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.Composables.components.TopBar
import com.example.expensetracker.Composables.greenColor
import com.example.expensetracker.Composables.orange
import com.example.expensetracker.Composables.secondaryAlpha
import com.example.expensetracker.Composables.utils.IconState
import com.example.expensetracker.Composables.utils.combineColors
import com.example.expensetracker.Model.CategoryClass
import com.example.expensetracker.Utilities.Constants.EXPENSE
import com.example.expensetracker.ViewModels.CategoryViewModel

@Composable
fun CategoryScreen(
    sendUserBack: () -> Unit,
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    val allExpenseCategory by categoryViewModel.onlyExpenseCategoryNames.collectAsStateWithLifecycle()
    val allIncomeCategory by categoryViewModel.onlyIncomeCategoryNames.collectAsStateWithLifecycle()
    val lazyList = rememberLazyListState()
    val scrollState = rememberScrollState()
    Surface {
        Column(Modifier.verticalScroll(scrollState)) {
            TopBar("Category") { sendUserBack() }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Income Category", style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(startEndPadding, topBottomPadding)
            )
            Text(
                text = "This categories will be shown when the transaction type is expense",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha),
                modifier = Modifier.padding(startEndPadding, topBottomPadding)
            )

            Spacer(Modifier.height(4.dp))

            Column {
                for (i in allIncomeCategory.chunked(2)) {
                    Row(modifier = Modifier) {
                        // First item in the row
                        i.getOrNull(0)?.let {
                            SingleCategory(
                                Modifier
                                    .weight(1f)
                                    .padding(
                                        start = 16.dp,
                                        end = 8.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                it
                            ) { }
                        }

                        // Second item in the row
                        i.getOrNull(1)?.let {
                            SingleCategory(
                                Modifier
                                    .weight(1f)
                                    .padding(
                                        start = 8.dp,
                                        end = 16.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                it
                            ) { }

                        }
                    }

                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Income Category", style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(startEndPadding, topBottomPadding)
            )
            Text(
                text = "This categories will be shown when the transaction type is expense",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(secondaryAlpha),
                modifier = Modifier.padding(startEndPadding, topBottomPadding)
            )

            Spacer(Modifier.height(4.dp))

            Column {
                for (i in allExpenseCategory.chunked(2)) {
                    Row(modifier = Modifier) {
                        // First item in the row
                        i.getOrNull(0)?.let {
                            SingleCategory(
                                Modifier
                                    .weight(0.5f)
                                    .padding(
                                        start = 16.dp,
                                        end = 8.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                it
                            ) { }
                        }

                        // Second item in the row
                        i.getOrNull(1)?.let {
                            SingleCategory(
                                Modifier
                                    .weight(0.5f)
                                    .padding(
                                        start = 8.dp,
                                        end = 16.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                it
                            ) { }

                        }
                    }
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }

}


@Composable
fun SingleCategory(
    modifier: Modifier,
    item: CategoryClass,
    onSingleItemClick: (CategoryClass) -> Unit
) {
    val containerColor = combineColors(
        MaterialTheme.colorScheme.surface,
        if (item.categoryType == EXPENSE) orange else greenColor,
        angle = 0.1f,
    )
    Card(
        shape = RoundedCornerShape(12.dp), modifier = modifier
            .clickable {
                onSingleItemClick(item)
            }, colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(36.dp)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        val image =
                            rememberAsyncImagePainter(IconState.fromNumber(item.categoryIconNumber))
                        Image(
                            painter = image,
                            contentDescription = "Test Image",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }


                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth(0.70f)

                ) {
                    Text(
                        text = item.categoryName.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                    )
                    if (false) {
                        Text(
                            text = item.categoryType,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        )
                    }
                }

//                Spacer(modifier = Modifier.width(16.dp))
            }
//            IconButton(
//                onClick = {}
//            ) { Icon(Icons.Filled.Delete, contentDescription = "delete") }
        }
    }
}

@Preview
@Composable
private fun SingleCategoryPreview() {
    ExpenseTrackerTheme {
        SingleCategory(
            Modifier,
            CategoryClass(
                id = 1,
                categoryName = "Food & Drinks",
                categoryColorNumber = 1,
                categoryIconNumber = 2,
                categoryType = "Expense"
            )
        ) { }
    }
}