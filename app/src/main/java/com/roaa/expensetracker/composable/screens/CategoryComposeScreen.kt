package com.roaa.expensetracker.composable.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.composable.components.CategoryActionConfirmation
import com.roaa.expensetracker.composable.components.CategoryDetailsBottomSheet
import com.roaa.expensetracker.composable.components.CircularProgress
import com.roaa.expensetracker.composable.components.TopBar
import com.roaa.expensetracker.composable.greenColor
import com.roaa.expensetracker.composable.navigation.Destinations
import com.roaa.expensetracker.composable.navigation.NavigationManager
import com.roaa.expensetracker.composable.navigation.handleBackNavigation
import com.roaa.expensetracker.composable.orange
import com.roaa.expensetracker.composable.utils.IconState
import com.roaa.expensetracker.composable.utils.combineColors
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.model.CategoryClass
import com.roaa.expensetracker.utilities.Constants.EXPENSE
import com.roaa.expensetracker.utilities.DeleteAction
import com.roaa.expensetracker.utilities.UiState
import com.roaa.expensetracker.utilities.utilityModalClass.defaultCategoryClass
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoryScreen(
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    viewModel: AllViewModel,
) {
    val context = LocalContext.current
    BackHandler {
        handleBackNavigation(navigationManager)
    }
    Surface {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Category",
                    showDelete = false,
                    sendUserBackToPreviousActivity = { handleBackNavigation(navigationManager) },
                    delete = {}
                )
            },
            content = { paddingValues ->
                ScaffoldContent(
                    Modifier.padding(paddingValues),
                    navigationManager,
                    viewModel,
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        navigationManager.navController.navigate(
                            Destinations.CategoryDetailsScreen(
                                0L,
                                "",
                                99,
                                EXPENSE
                            )
                        )
                    },
                    icon = { Icon(Icons.Filled.Add, "Add Category") },
                    text = { Text(text = "Add Category") },
                )
            },
            floatingActionButtonPosition = FabPosition.EndOverlay
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ScaffoldContent(
    modifier: Modifier = Modifier,
    navigationManager: NavigationManager,
    viewModel: AllViewModel,
) {
    val uiState by viewModel.categoryViewModel.uiState.collectAsState()
    val allExpenseCategoryList by viewModel.categoryViewModel.onlyExpenseCategoryNames.collectAsStateWithLifecycle()
    val allIncomeCategoryList by viewModel.categoryViewModel.onlyIncomeCategoryNames.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var specificTransactionButtonClicked by remember { mutableStateOf(false) }
    var showCategoryDetailBottomSheet by remember { mutableStateOf(false) }
    var categoryClass by remember { mutableStateOf(defaultCategoryClass) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var scope = rememberCoroutineScope()

    when (uiState) {
        is UiState.Loading -> {
            CircularProgress()
        }

        is UiState.Success -> {
            Column(
                modifier
                    .verticalScroll(scrollState)
            ) {

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Income Category", style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(startEndPadding, topBottomPadding)
                )
//                Text(
//                    text = "This categories will be shown when the transaction type is expense",
//                    style = MaterialTheme.typography.labelLarge,
//                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
//                    modifier = Modifier.padding(startEndPadding, topBottomPadding)
//                )

                Spacer(Modifier.height(4.dp))
                Column {
                    for (i in allIncomeCategoryList.chunked(2)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Spacer(Modifier.width(16.dp))
                            // First item in the row
                            i.getOrNull(0)?.let {
                                SingleCategory(
                                    Modifier
                                        .weight(0.5f)
                                        .padding(
                                            top = 8.dp,
                                            bottom = 8.dp
                                        ),
                                    it,
                                ) {
//                                    navigationManager.navController.navigate(
//                                        Destinations.CategoryDetailsScreen(
//                                            it.categoryId,
//                                            it.categoryName,
//                                            it.categoryIconNumber,
//                                            it.categoryType
//                                        )
//                                    )
                                    categoryClass = it
                                    showCategoryDetailBottomSheet = !showCategoryDetailBottomSheet
                                }
                            }

                            // Second item in the row
                            i.getOrNull(1)?.let {
                                Spacer(Modifier.width(16.dp))
                                SingleCategory(
                                    Modifier
                                        .weight(0.5f)
                                        .padding(
                                            top = 8.dp,
                                            bottom = 8.dp
                                        ),
                                    it
                                ) {
//                                    navigationManager.navController.navigate(
//                                        Destinations.CategoryDetailsScreen(
//                                            it.categoryId,
//                                            it.categoryName,
//                                            it.categoryIconNumber,
//                                            it.categoryType
//                                        )
//                                    )
                                    categoryClass = it
                                    showCategoryDetailBottomSheet = !showCategoryDetailBottomSheet
                                }

                            }
                            Spacer(Modifier.width(16.dp))
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Expense Category", style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(startEndPadding, topBottomPadding)
                )
//                Text(
//                    text = "This categories will be shown when the transaction type is expense",
//                    style = MaterialTheme.typography.labelLarge,
//                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
//                    modifier = Modifier.padding(startEndPadding, topBottomPadding)
//                )

                Spacer(Modifier.height(4.dp))

                Column {
                    for (i in allExpenseCategoryList.chunked(2)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Spacer(Modifier.width(16.dp))
                            // First item in the row
                            i.getOrNull(0)?.let {
                                SingleCategory(
                                    Modifier
                                        .weight(0.5f)
                                        .padding(
                                            top = 8.dp,
                                            bottom = 8.dp
                                        ),
                                    it,
                                ) {
//                                    navigationManager.navController.navigate(
//                                        Destinations.CategoryDetailsScreen(
//                                            it.categoryId,
//                                            it.categoryName,
//                                            it.categoryIconNumber,
//                                            it.categoryType
//                                        )
//                                    )
                                    categoryClass = it
                                    showCategoryDetailBottomSheet = !showCategoryDetailBottomSheet
                                }
                            }

                            // Second item in the row
                            i.getOrNull(1)?.let {
                                Spacer(Modifier.width(16.dp))
                                SingleCategory(
                                    Modifier
                                        .weight(0.5f)
                                        .padding(
                                            top = 8.dp,
                                            bottom = 8.dp
                                        ),
                                    it,
                                ) {
//                                    navigationManager.navController.navigate(
//                                        Destinations.CategoryDetailsScreen(
//                                            it.categoryId,
//                                            it.categoryName,
//                                            it.categoryIconNumber,
//                                            it.categoryType
//                                        )
//                                    )
                                    categoryClass = it
                                    showCategoryDetailBottomSheet = !showCategoryDetailBottomSheet
                                }
                            }
                            Spacer(Modifier.width(16.dp))
                        }

                    }

                    Spacer(Modifier.height(64.dp))
                }
            }
        }

        is UiState.Error -> {}
    }

    if (showCategoryDetailBottomSheet) {
        CategoryDetailsBottomSheet(
            categoryClass = categoryClass,
            onDismiss = { showCategoryDetailBottomSheet = !showCategoryDetailBottomSheet },
            editButtonClicked = {
                showCategoryDetailBottomSheet = !showCategoryDetailBottomSheet
                navigationManager.navigateTo(
                    Destinations.CategoryDetailsScreen(
                        it.categoryId,
                        it.categoryName,
                        it.categoryIconNumber,
                        it.categoryType
                    )
                )
            },

            deleteButtonClicked = { showDeleteConfirmationDialog = !showDeleteConfirmationDialog },
            statAnalysisClicked = {
                navigationManager.navigateTo(Destinations.StatisticsScreen)
                showCategoryDetailBottomSheet = !showCategoryDetailBottomSheet
            },
            specificTransactionsClicked = {
                navigationManager.navigateTo(Destinations.ListScreen)
                showCategoryDetailBottomSheet = !showCategoryDetailBottomSheet
                scope.launch {
                    viewModel.uiViewModel.addCategorySpecificOrBankSpecificTransaction.emit(
                        true
                    )
                    viewModel.uiViewModel.addSpecificCategoryForTransaction.emit(categoryClass)
                }
            }
        )
    }
    if (showDeleteConfirmationDialog) {
        CategoryActionConfirmation(
            modifier = Modifier,
            shouldEnableTheMigration = true,
            categoryClass = categoryClass,
            categoryClassList = if (categoryClass.categoryType == EXPENSE) allExpenseCategoryList else allIncomeCategoryList,
            performAction = { action, targetCategoryClass ->
                when (action) {
                    DeleteAction.DELETE -> {
                        scope.launch {
                            viewModel.categoryViewModel.storeCategoryInDatabase(categoryClass.apply {
                                this.isActive = false
                            })
                        }
                    }

                    DeleteAction.DELETE_AND_MIGRATE -> {
                        viewModel.categoryViewModel.migrateCategoryTransactions(
                            categoryClass, targetCategoryClass
                        )
                    }

                    DeleteAction.DELETE_ALL_WITH_TRANSACTIONS -> {
                        viewModel.categoryViewModel.deleteCategoryWithTransactions(
                            categoryClass
                        )
                    }

                }
                showDeleteConfirmationDialog = !showDeleteConfirmationDialog
                handleBackNavigation(
                    navigationManager
                )
            },
            onDismissRequest = { showDeleteConfirmationDialog = !showDeleteConfirmationDialog }
        )
    }
//    if (specificTransactionButtonClicked) {
//        FilterTransactionScreenDialog(
//            modifier = Modifier,
//            { specificTransactionButtonClicked = !specificTransactionButtonClicked })
//    }
}

//@Composable
//fun CategoryClicked(it: CategoryClass, navController: NavHostController) {
//    val navController = rememberNavController()
//    SendUserToAddCategoryActivity(navController)
//}

@OptIn(ExperimentalSharedTransitionApi::class)
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
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onSingleItemClick(item)
            }, colors = CardDefaults.cardColors(
            containerColor = containerColor,

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
                        modifier = Modifier
                            .fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        val image =
                            rememberAsyncImagePainter(IconState.fromNumber(item.categoryIconNumber))
                        Image(
                            painter = image,
                            contentDescription = "Image ${item.categoryIconNumber}",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                }


                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth(1f)

                ) {
                    Text(
                        text = item.categoryName.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
