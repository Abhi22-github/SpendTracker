package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import com.roaa.expensetracker.Composables.Navigation.Destinations
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.Composables.greenColor
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.ViewModels.CategoryViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoryScreen(
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
) {
    with(sharedTransitionScope) {
        val context = LocalContext.current
        fun handleBackNavigation() {
            if (navigationManager.navController.previousBackStackEntry != null) {
                navigationManager.navController.popBackStack() // Pop one screen if there is a back stack
            } else {
                navigationManager.navController.navigate(Destinations.ListScreen) {
                    popUpTo(Destinations.ListScreen) { inclusive = true }
                }
            }
        }
        BackHandler {
            handleBackNavigation()
        }
        Surface {
            Scaffold(
                topBar = {
                    TopBar(
                        title = "Category",
                        showDelete = false,
                        sendUserBackToPreviousActivity = { handleBackNavigation() },
                        delete = {}
                    )
                },
                content = { paddingValues ->
                    ScaffoldContent(
                        Modifier.padding(paddingValues),
                        navigationManager,
                        sharedTransitionScope,
                        animatedVisibilityScope
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

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ScaffoldContent(
    modifier: Modifier = Modifier,
    navigationManager: NavigationManager,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    val allExpenseCategory by categoryViewModel.onlyExpenseCategoryNames.collectAsStateWithLifecycle()
    val allIncomeCategory by categoryViewModel.onlyIncomeCategoryNames.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

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
        Text(
            text = "This categories will be shown when the transaction type is expense",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
            modifier = Modifier.padding(startEndPadding, topBottomPadding)
        )

        Spacer(Modifier.height(4.dp))

//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2), // 2 columns
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            contentPadding = PaddingValues(16.dp) // Optional padding for content
//        ) {
//            items(allIncomeCategory) { item ->
//                SingleCategory(
//                    Modifier
//                        .weight(0.5f)
//                        .padding(
//                            top = 8.dp,
//                            bottom = 8.dp
//                        ),
//                    item,
//                    sharedTransitionScope, animatedVisibilityScope
//                ) {
//                    navController.navigate(
//                        ScreenB(
//                            it.id,
//                            it.categoryName,
//                            it.categoryIconNumber,
//                            it.categoryType
//                        )
//                    )
//                }
//            }
//
//        }


        Column {
            for (i in allIncomeCategory.chunked(2)) {
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
                            sharedTransitionScope, animatedVisibilityScope
                        ) {
                            navigationManager.navController.navigate(
                                Destinations.CategoryDetailsScreen(
                                    it.categoryId,
                                    it.categoryName,
                                    it.categoryIconNumber,
                                    it.categoryType
                                )
                            )
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
                            sharedTransitionScope, animatedVisibilityScope
                        ) {
                            navigationManager.navController.navigate(
                                Destinations.CategoryDetailsScreen(
                                    it.categoryId,
                                    it.categoryName,
                                    it.categoryIconNumber,
                                    it.categoryType
                                )
                            )
                        }

                    }
                    Spacer(Modifier.width(16.dp))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Income Category", style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(startEndPadding, topBottomPadding)
        )
        Text(
            text = "This categories will be shown when the transaction type is expense",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
            modifier = Modifier.padding(startEndPadding, topBottomPadding)
        )

        Spacer(Modifier.height(4.dp))

        Column {
            for (i in allExpenseCategory.chunked(2)) {
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
                            sharedTransitionScope, animatedVisibilityScope
                        ) {
                            navigationManager.navController.navigate(
                                Destinations.CategoryDetailsScreen(
                                    it.categoryId,
                                    it.categoryName,
                                    it.categoryIconNumber,
                                    it.categoryType
                                )
                            )
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
                            sharedTransitionScope, animatedVisibilityScope
                        ) {
                            navigationManager.navController.navigate(
                                Destinations.CategoryDetailsScreen(
                                    it.categoryId,
                                    it.categoryName,
                                    it.categoryIconNumber,
                                    it.categoryType
                                )
                            )
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                }

            }

            Spacer(Modifier.height(64.dp))
        }
    }
}

//@Composable
//fun CategoryClicked(it: CategoryClass, navController: NavHostController) {
//    val navController = rememberNavController()
//    SendUserToAddCategoryActivity(navController)
//}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SingleCategory(
    modifier: Modifier,
    item: CategoryClass,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    onSingleItemClick: (CategoryClass) -> Unit
) {
    with(sharedTransitionScope) {
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
                                    .sharedElement(
                                        state = rememberSharedContentState(key = "image/${item.categoryId}"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    ),
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
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState(key = "text/${item.categoryId}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                            )
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
}

@Preview
@Composable
private fun SingleCategoryPreview() {
    ExpenseTrackerTheme {
//        SingleCategory(
//            Modifier,
//            CategoryClass(
//                id = 1,
//                categoryName = "Food & Drinks",
//                categoryColorNumber = 1,
//                categoryIconNumber = 2,
//                categoryType = "Expense"
//            )
//        ) { }
    }
}