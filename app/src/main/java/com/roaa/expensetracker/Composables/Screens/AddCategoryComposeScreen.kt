package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import com.roaa.expensetracker.Composables.Navigation.Destinations
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.Navigation.onBackPressed
import com.roaa.expensetracker.Composables.components.BottomSheetIconPicker
import com.roaa.expensetracker.Composables.components.ConfirmationAlertDialog
import com.roaa.expensetracker.Composables.components.ErrorRow
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.Composables.greenColor
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.secondaryAlpha
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.R
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.Constants.INCOME
import com.roaa.expensetracker.ViewModels.CategoryViewModel
import com.roaa.expensetracker.ViewModels.UiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddCategory(
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    categoryId: Long,
    categoryName: String,
    categoryIcon: Int,
    categoryType: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    uiViewModel: UiViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    val category = CategoryClass(categoryId, categoryName, 1, categoryIcon, categoryType, true)
    val showDeleteButton by remember { mutableStateOf(if (category.categoryId == 0L) false else true) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var confirmationDialogType by remember { mutableIntStateOf(1) }
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

    BackHandler() {
        handleBackNavigation()
    }

    with(sharedTransitionScope) {
        ExpenseTrackerTheme {
            Scaffold(topBar = {
                TopBar(
                    title = if (categoryId == 0L) "Add Category" else "Edit Category",
                    showDelete = true,
                    sendUserBackToPreviousActivity = { handleBackNavigation() },
                    delete = {
                        confirmationDialogType = 1
                        showConfirmationDialog = true
                    }
                )
            }) { paddingValues ->
                ScaffoldContentDetails(
                    Modifier.padding(paddingValues),
                    category,
                    { rootNavController.onBackPressed() },
                    animatedVisibilityScope
                )
            }
        }
    }
    if (showConfirmationDialog) {
        //for confirming the delete action
        if (confirmationDialogType == 1) {
            ConfirmationAlertDialog(
                onDismissRequest = { showConfirmationDialog = !showConfirmationDialog },
                onConfirmation = {
                    categoryViewModel.deleteCategoryFromDatabase(category)
                    showConfirmationDialog = !showConfirmationDialog
                    rootNavController.onBackPressed()

                },
                dialogTitle = "Delete Category",
                dialogText = "Are you sure, you want to delete the current category",
                icon = ImageVector.vectorResource(R.drawable.icon_expense)
            )
        }
        //for confirming the update action
        if (confirmationDialogType == 2) {
            ConfirmationAlertDialog(
                onDismissRequest = { showConfirmationDialog = !showConfirmationDialog },
                onConfirmation = {
                    categoryViewModel.deleteCategoryFromDatabase(category)
                    showConfirmationDialog = !showConfirmationDialog
                    rootNavController.onBackPressed()
                },
                dialogTitle = "Update Category",
                dialogText = "Are you sure, you want to update the current category",
                icon = ImageVector.vectorResource(R.drawable.icon_expense)
            )
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ScaffoldContentDetails(
    modifier: Modifier = Modifier,
    categoryClass: CategoryClass,
    backButtonClick: () -> Unit,
    animatedVisibilityScope: AnimatedContentScope,
    uiViewModel: UiViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    var categoryName by remember { mutableStateOf(categoryClass.categoryName) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectedIcon by uiViewModel.selectedIconFromBottomSheet.collectAsState()
    var bottomSheetStatus by remember { mutableStateOf(true) }
    var selectedIndex by remember { mutableIntStateOf(if (categoryClass.categoryType == EXPENSE) 0 else 1) }
    val errorStatus by uiViewModel.errorStatusInAddCategory.collectAsState(false)
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val containerColor by animateColorAsState(
        targetValue = combineColors(
            MaterialTheme.colorScheme.surface,
            if (selectedIndex == 0) orange else greenColor,
            angle = 0.1f,
        )
    )
    LaunchedEffect(selectedIcon) {

        scope.launch {
            uiViewModel.errorStatusInAddCategory.emit(false)
            if (categoryClass.categoryIconNumber != 99) {
                uiViewModel.selectedIconFromBottomSheet.emit(categoryClass.categoryIconNumber)
            }
        }

    }


    BoxWithConstraints(Modifier.fillMaxSize()) {
        Column(
            modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val options = listOf("Expense", "Income")

            Spacer(Modifier.height(12.dp))

            Column {
                TextSwitch(
                    selectedIndex = selectedIndex,
                    items = options,
                    onSelectionChange = {
                        selectedIndex = it
                    }
                )
            }

            Spacer(Modifier.height(40.dp))
            ErrorRow(errorStatus)

            Spacer(Modifier.height(40.dp))
            Box() {
                Surface(
                    shape = RoundedCornerShape(20),
                    modifier = Modifier
                        .size(160.dp)
                        .fillMaxSize(),
                    color = containerColor
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        val image =
                            rememberAsyncImagePainter(IconState.fromNumber(if (selectedIcon == 99) categoryClass.categoryIconNumber else selectedIcon))
                        Image(
                            painter = image,
                            contentDescription = "Image ${categoryClass.categoryIconNumber}",
                            modifier = Modifier
                                .size(96.dp)
                                .sharedElement(
                                    state = rememberSharedContentState(key = "image/${categoryClass.categoryId}"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                ),
                        )
                        //  }
                    }
                }
                FilledTonalIconButton(
                    onClick = {
                        bottomSheetStatus = !bottomSheetStatus
                    },
                    Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 8.dp, y = 8.dp)
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "edit")
                }
            }

            Spacer(Modifier.height(32.dp))

            TextField(
                value = categoryName,
                onValueChange = { newText ->
                    categoryName = newText
                    scope.launch {
                        uiViewModel.errorStatusInAddCategory.emit(false)
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 8.dp)
                    .sharedElement(
                        state = rememberSharedContentState(key = "text/${categoryClass.categoryId}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
                placeholder = {
                    Text(
                        "Category Name",
                        Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(secondaryAlpha),
                        textAlign = TextAlign.Center
                    )
                },
                shape = RoundedCornerShape(36.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
            )

            Spacer(Modifier.height(12.dp))

            /*Button*/
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 8.dp),
                onClick = {
                    scope.launch {
                        if (categoryName.isEmpty()) {
                            uiViewModel.errorStatusInAddCategory.emit(true)
                            uiViewModel.errorStatusMessage.emit("Please provide the category name")
                            return@launch
                        }
                        if (selectedIcon == 99) {
                            uiViewModel.errorStatusInAddCategory.emit(true)
                            uiViewModel.errorStatusMessage.emit("Please select icon for Category")
                            return@launch
                        }
                        if (categoryClass.categoryId != 0L) {
                            showConfirmationDialog = true
                            return@launch
                        } else {
                            storeCategoryData(
                                categoryClass.categoryId,
                                categoryName,
                                selectedIcon,
                                selectedIndex,
                                scope,
                                categoryViewModel,
                                backButtonClick = { backButtonClick() }
                            )
                        }
                    }

                },
            ) {
                Text(
                    text = "Save", Modifier.padding(12.dp, 6.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }


        }
    }

    if (bottomSheetStatus) {
        BottomSheetIconPicker(bottomSheetState) {
            bottomSheetStatus = !bottomSheetStatus
        }
    }
    if (showConfirmationDialog) {
        ConfirmationAlertDialog(
            onDismissRequest = { showConfirmationDialog = !showConfirmationDialog },
            onConfirmation = {
                storeCategoryData(
                    categoryClass.categoryId,
                    categoryName,
                    selectedIcon,
                    selectedIndex,
                    scope,
                    categoryViewModel,
                    backButtonClick = { backButtonClick() }
                )
            },
            dialogTitle = "Save Changes",
            dialogText = "Are you sure, that you want to update current category",
            icon = ImageVector.vectorResource(R.drawable.icon_expense)
        )
    }

}

fun storeCategoryData(
    categoryId: Long,
    categoryName: String,
    selectedIcon: Int,
    selectedType: Int,
    scope: CoroutineScope,
    categoryViewModel: CategoryViewModel,
    backButtonClick: () -> Unit = {}
) {
    scope.launch {
        categoryViewModel.validateCategoryData(
            categoryId,
            categoryName,
            selectedIcon,
            if (selectedType == 0) EXPENSE else INCOME
        )
        backButtonClick()
    }

}


fun ContentDrawScope.drawWithLayer(block: ContentDrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }
}

@Composable
fun TextSwitch(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    items: List<String>,
    onSelectionChange: (Int) -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceContainerLow
    val selectedButtonColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val selectedButtonTextColor = MaterialTheme.colorScheme.onSurface

    BoxWithConstraints(
        modifier
            .padding(8.dp)
            .width(250.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(35.dp))
            .background(surfaceColor)
            .padding(8.dp)
    ) {
        if (items.isNotEmpty()) {

            val maxWidth = this.maxWidth
            val tabWidth = maxWidth / items.size

            val indicatorOffset by animateDpAsState(
                targetValue = tabWidth * selectedIndex,
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
                label = "indicator offset"
            )

            // This is for shadow layer matching white background
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .shadow(0.dp, RoundedCornerShape(35.dp))
                    .width(tabWidth)
                    .fillMaxHeight()

            )

            Row(modifier = Modifier
                .fillMaxWidth()

                .drawWithContent {

                    //   This is for setting black text while drawing on white background
                    val padding = 8.dp.toPx()
                    drawRoundRect(
                        topLeft = Offset(x = indicatorOffset.toPx() + padding, padding),
                        size = Size(size.width / 2 - padding * 2, size.height - padding * 2),
                        color = selectedButtonTextColor,
                        cornerRadius = CornerRadius(x = 35.dp.toPx(), y = 35.dp.toPx()),
                    )

                    drawWithLayer {
                        drawContent()

                        // This is white top rounded rectangle
                        drawRoundRect(
                            topLeft = Offset(x = indicatorOffset.toPx(), 0f),
                            size = Size(size.width / 2, size.height),
                            color = selectedButtonColor,
                            cornerRadius = CornerRadius(x = 35.dp.toPx(), y = 35.dp.toPx()),
                            blendMode = BlendMode.SrcOut
                        )
                    }

                }
            ) {
                items.forEachIndexed { index, text ->
                    Box(
                        modifier = Modifier
                            .width(tabWidth)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null,
                                onClick = {
                                    onSelectionChange(index)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThreeOptionTextSwitch(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    items: List<String>,
    onSelectionChange: (Int) -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceContainerLow
    val selectedButtonColor = MaterialTheme.colorScheme.secondaryContainer
    val selectedButtonTextColor = MaterialTheme.colorScheme.onSecondaryContainer

    BoxWithConstraints(
        modifier
            .width(250.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(35.dp))
            .background(surfaceColor)

    ) {
        if (items.isNotEmpty()) {

            val maxWidth = this.maxWidth
            val tabWidth = maxWidth / items.size

            val indicatorOffset by animateDpAsState(
                targetValue = tabWidth * selectedIndex,
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
                label = "indicator offset"
            )

            // This is for shadow layer matching white background
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .shadow(0.dp, RoundedCornerShape(25.dp))
                    .width(tabWidth)
                    .fillMaxHeight()

            )

            Row(modifier = Modifier
                .fillMaxWidth()

                .drawWithContent {

                    //   This is for setting black text while drawing on white background
                    val padding = 8.dp.toPx()
                    drawRoundRect(
                        topLeft = Offset(x = indicatorOffset.toPx() + padding, padding),
                        size = Size(size.width / 3 - padding , size.height - padding * 2),
                        color = selectedButtonTextColor,
                        cornerRadius = CornerRadius(x = 35.dp.toPx(), y = 35.dp.toPx()),
                    )

                    drawWithLayer {
                        drawContent()

                        // This is white top rounded rectangle
                        drawRoundRect(
                            topLeft = Offset(x = indicatorOffset.toPx(), 0f),
                            size = Size(size.width / 3, size.height),
                            color = selectedButtonColor,
                            cornerRadius = CornerRadius(x = 25.dp.toPx(), y = 25.dp.toPx()),
                            blendMode = BlendMode.SrcOut
                        )
                    }

                }
            ) {
                items.forEachIndexed { index, text ->
                    Box(
                        modifier = Modifier
                            .width(tabWidth)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null,
                                onClick = {
                                    onSelectionChange(index)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
                        )
                    }
                }
            }
        }
    }
}

