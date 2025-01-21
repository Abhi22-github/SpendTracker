package com.example.expensetracker.Composables.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    val allCategory by categoryViewModel.allCategoryList.collectAsStateWithLifecycle()
    val lazyList = rememberLazyListState()
    Surface {
        Column {
            TopBar("Category") { sendUserBack() }
            LazyColumn(state = lazyList, modifier = Modifier.fillMaxWidth()) {
                items(allCategory, key = { it.id }) {
                    SingleCategory(it) { }
                }
            }
        }
    }

}


@Composable
fun SingleCategory(item: CategoryClass, onSingleItemClick: (CategoryClass) -> Unit) {
    val containerColor = combineColors(
        MaterialTheme.colorScheme.surface,
        if (item.categoryType == EXPENSE) orange else greenColor,
        angle = 0.1f,
    )
    Card(
        shape = RoundedCornerShape(12.dp), modifier = Modifier
            .padding(16.dp, 4.dp)
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
                .padding(16.dp, 6.dp),
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
                        color = MaterialTheme.colorScheme.onSurface
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
            IconButton(
                onClick = {}
            ) { Icon(Icons.Filled.Delete, contentDescription = "delete") }
        }
    }
}

@Preview
@Composable
private fun SingleCategoryPreview() {
    ExpenseTrackerTheme {
        SingleCategory(
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