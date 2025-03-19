package com.roaa.expensetracker.composable.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.R
import com.roaa.expensetracker.composable.ThemeMode
import com.roaa.expensetracker.composable.components.DropDownMenu
import com.roaa.expensetracker.composable.components.SingleItemRadioButton
import com.roaa.expensetracker.composable.components.TopBar
import com.roaa.expensetracker.composable.greenColor
import com.roaa.expensetracker.composable.navigation.NavigationManager
import com.roaa.expensetracker.composable.navigation.handleBackNavigation
import com.roaa.expensetracker.composable.orange
import com.roaa.expensetracker.composable.utils.IconState
import com.roaa.expensetracker.composable.utils.combineColors
import com.roaa.expensetracker.composable.utils.toPalette
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.model.uiDataModels.CurrencyClass
import com.roaa.expensetracker.utilities.getCountryCurrencyList
import com.roaa.expensetracker.utilities.utilityModalClass.defaultCurrency
import com.roaa.expensetracker.utilities.utilityModalClass.firstSampleClass
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

val radioButtonColors
    @Composable
    @ReadOnlyComposable
    get() = RadioButtonColors(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.surface
    )

@Composable
fun SettingsScreen(
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    viewModel: AllViewModel,
    sendUserBack: () -> Unit,
) {

    BackHandler() {
        handleBackNavigation(navigationManager)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Settings",
                showDelete = false,
                sendUserBackToPreviousActivity = { handleBackNavigation(navigationManager) },
                delete = {})
        },
    ) {
        SettingsScreenContent(Modifier.padding(it), viewModel)
    }
}

val startEndPadding = 16.dp
val topBottomPadding = 0.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: AllViewModel
) {
    val themeSelected by viewModel.preferencesViewModel.getThemeMode.collectAsState(ThemeMode.SYSTEM.toString())
    val showExperimentalComponent by viewModel.preferencesViewModel.showExperimentalComponent.collectAsState(
        false
    )
    val isPreDefaultCategorySet by viewModel.preferencesViewModel.getPreDefaultCategoryStatus.collectAsState(
        false
    )
    var expenseDropDownStatus by remember { mutableStateOf(false) }
    var incomeDropDownStatus by remember { mutableStateOf(false) }
    val expenseCategoryId by viewModel.preferencesViewModel.getLastExpenseCategory.collectAsState(0L)
    val incomeCategoryId by viewModel.preferencesViewModel.getLastIncomeCategory.collectAsState(0L)
    val list = listOf("LIGHT", "NIGHT", "SYSTEM")
    val scope = rememberCoroutineScope()
    var showCurrencyDialog by remember { mutableStateOf(false) }
    val currentCurrency by viewModel.preferencesViewModel.getCurrency.collectAsState(defaultCurrency)
    val expenseCategoryList by viewModel.categoryViewModel.onlyExpenseCategoryNames.collectAsState()
    val incomeCategoryList by viewModel.categoryViewModel.onlyIncomeCategoryNames.collectAsState()
    var selectedExpenseCategory by remember {
        mutableStateOf(
            firstSampleClass
        )
    }
    var selectedIncomeCategory by remember {
        mutableStateOf(
            firstSampleClass
        )
    }
//for email
    val context = LocalContext.current
    val emailIntent = remember {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Ensures only email apps handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf("roaatech22@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback for Expense Tracker App")
        }
    }
    //for share
    val appPackageName = context.packageName // Get your app's package name
    val shareIntent = remember {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this amazing Expense Tracker app!")
            putExtra(
                Intent.EXTRA_TEXT,
                "Hey! Try this Expense Tracker app: https://play.google.com/store/apps/details?id=$appPackageName"
            )
        }
    }
    //for Rate us
    val rateIntent = remember {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$appPackageName")
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    LaunchedEffect(expenseCategoryList, expenseCategoryId) {
        val expenseCategoryPresent =
            expenseCategoryList.any { it.categoryId == expenseCategoryId }
        if (expenseCategoryPresent) {
            selectedExpenseCategory =
                expenseCategoryList.first { it.categoryId == expenseCategoryId }
        }
    }
    LaunchedEffect(incomeCategoryList, incomeCategoryId) {
        val incomeCategoryPresent =
            incomeCategoryList.any { it.categoryId == incomeCategoryId }
        if (incomeCategoryPresent) {
            selectedIncomeCategory = incomeCategoryList.first { it.categoryId == incomeCategoryId }
        }
    }

    Column(modifier) {
        Column() {
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Theme", style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(startEndPadding, topBottomPadding)
            )
            Text(
                text = "Please select theme according to your preferences or you can set it according to your device theme",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
                modifier = Modifier.padding(startEndPadding, topBottomPadding)
            )
            Column(modifier = Modifier.padding(startEndPadding, topBottomPadding)) {
                list.map {
                    SingleItemRadioButton(
                        it,
                        selectedItem = { viewModel.preferencesViewModel.saveTheme(it) },
                        themeSelected
                    )
                }
            }

//            Spacer(Modifier.height(32.dp))
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .padding(horizontalPadding, verticalPadding)
//            ) {
//                Column(Modifier.weight(1f)) {
//                    Text(
//                        text = "Experiment Components",
//                        style = MaterialTheme.typography.titleMedium,
//                        color = MaterialTheme.colorScheme.onSurface,
//
//                        )
//                    Text(
//                        text = "Show all experimental components",
//                        style = MaterialTheme.typography.labelLarge,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
//                    )
//                }
//                Row(
//                    modifier = Modifier
//                        .weight(.3f), horizontalArrangement = Arrangement.End
//                ) {
//                    Switch(
//                        checked = showExperimentalComponent,
//                        onCheckedChange = {
//                            scope.launch {
//                                viewModel.preferencesViewModel.setExperimentalComponentsState(it)
//                            }
//                        },
//                        modifier = Modifier
//
//                    )
//                }
//            }

            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.clickable { showCurrencyDialog = !showCurrencyDialog }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontalPadding, 16.dp)

                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "Currency", style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
//                    Text(
//                        text = "",
//                        style = MaterialTheme.typography.labelLarge,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
//                        modifier = Modifier.padding(startEndPadding, topBottomPadding)
//                    )
                    }
                    Text(
                        textAlign = TextAlign.End,
                        text = "${currentCurrency.currencyDisplayName}(${currentCurrency.currencySymbol})",
                        modifier = Modifier
                            .weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontalPadding, verticalPadding)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "Preset Default Category",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "Automatically pre-fill a chosen category for faster expense entry",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .weight(.3f), horizontalArrangement = Arrangement.End
                    ) {
                        Switch(
                            checked = isPreDefaultCategorySet,
                            onCheckedChange = {
                                scope.launch {
                                    viewModel.preferencesViewModel.setPreDefaultCategoryStatus(it)
                                }
                            },
                            modifier = Modifier

                        )
                    }
                }
                AnimatedVisibility(isPreDefaultCategorySet) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                    ) {
                        Spacer(Modifier.height(14.dp))
                        Row(
                            Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .wrapContentHeight()

                            ) {
                                val colorPaletteOrange = toPalette(orange)
                                Button(
                                    modifier = Modifier.padding(end = 5.dp),
                                    onClick = { expenseDropDownStatus = !expenseDropDownStatus },
                                    colors = ButtonColors(
                                        containerColor = colorPaletteOrange.container.copy(alpha = 0.3f),
                                        contentColor = colorPaletteOrange.onContainer,
                                        disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
                                        disabledContentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    contentPadding = PaddingValues(
                                        start = 20.dp, end = 10.dp, top = 16.dp, bottom = 16.dp
                                    )
                                ) {
                                    Row {
                                        Row(Modifier.weight(0.8f)) {
                                            val image =
                                                rememberAsyncImagePainter(
                                                    IconState.fromNumber(
                                                        selectedExpenseCategory.categoryIconNumber
                                                    )
                                                )
                                            AnimatedContent(image) {
                                                Image(
                                                    painter = it,
                                                    contentDescription = "Test Image",
                                                    modifier = Modifier.size(24.dp),
                                                )
                                            }
                                            AnimatedContent(selectedExpenseCategory.categoryName) {
                                                Text(
                                                    text = it,
                                                    modifier = Modifier
                                                        .weight(0.6f)
                                                        .padding(start = 8.dp),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                        Icon(
                                            Icons.Filled.KeyboardArrowDown,
                                            "backIcon",
                                            modifier = Modifier.weight(0.2f)
                                        )
                                    }
                                }
                                DropDownMenu(
                                    Modifier,
                                    expenseDropDownStatus,
                                    selectedExpenseCategory,
                                    colorPaletteOrange,
                                    onDismiss = { expenseDropDownStatus = false },
                                    expenseCategoryList,
                                    selectedCategorySetter = {
                                        scope.launch {
                                            viewModel.preferencesViewModel.setLastUsedExpenseCategoryId(
                                                it.categoryId
                                            )
                                        }
                                    },
                                )

                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .wrapContentHeight()

                            ) {
                                val colorPalletGreen = toPalette(greenColor)
                                Button(
                                    modifier = Modifier.padding(end = 5.dp),
                                    onClick = { incomeDropDownStatus = !incomeDropDownStatus },
                                    colors = ButtonColors(
                                        containerColor = colorPalletGreen.container.copy(alpha = 0.3f),
                                        contentColor = colorPalletGreen.onContainer,
                                        disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
                                        disabledContentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    contentPadding = PaddingValues(
                                        start = 20.dp, end = 10.dp, top = 16.dp, bottom = 16.dp
                                    )
                                ) {
                                    Row {
                                        Row(Modifier.weight(0.8f)) {
                                            val image =
                                                rememberAsyncImagePainter(
                                                    IconState.fromNumber(
                                                        selectedIncomeCategory.categoryIconNumber
                                                    )
                                                )
                                            AnimatedContent(image) {
                                                Image(
                                                    painter = it,
                                                    contentDescription = "Test Image",
                                                    modifier = Modifier.size(24.dp),
                                                )
                                            }
                                            AnimatedContent(selectedIncomeCategory.categoryName) {
                                                Text(
                                                    text = it,
                                                    modifier = Modifier
                                                        .weight(0.6f)
                                                        .padding(start = 8.dp),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                        Icon(
                                            Icons.Filled.KeyboardArrowDown,
                                            "backIcon",
                                            modifier = Modifier.weight(0.2f)
                                        )
                                    }
                                }
                                DropDownMenu(
                                    Modifier,
                                    incomeDropDownStatus,
                                    selectedIncomeCategory,
                                    colorPalletGreen,
                                    onDismiss = { incomeDropDownStatus = false },
                                    incomeCategoryList,
                                    selectedCategorySetter = {
                                        scope.launch {
                                            viewModel.preferencesViewModel.setLastUsedIncomeCategoryId(
                                                it.categoryId
                                            )
                                        }
                                    },
                                )
                            }

                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.clickable { context.startActivity(emailIntent) }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontalPadding, 16.dp)

                ) {
                    Text(
                        text = "Feedback", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            Row(modifier = Modifier.clickable {
                context.startActivity(
                    Intent.createChooser(
                        shareIntent,
                        "Share via"
                    )
                )
            }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontalPadding, 16.dp)

                ) {
                    Text(
                        text = "Share", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            Row(modifier = Modifier.clickable {
                try {
                    context.startActivity(rateIntent)
                } catch (e: ActivityNotFoundException) {
                    // Fallback to Play Store website if the Play Store app is not installed
                    val webIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                    context.startActivity(webIntent)
                }
            }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontalPadding, 16.dp)

                ) {
                    Text(
                        text = "Rate us", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
    if (showCurrencyDialog) {
        Dialog(
            onDismissRequest = { showCurrencyDialog = !showCurrencyDialog },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.6f),
                shape = RoundedCornerShape(25.dp),
            ) {
                WorldCurrencyChooserContent(
                    defaultCurrency = currentCurrency,
                    onSelect = {
                        scope.launch {
                            viewModel.preferencesViewModel.setCurrency(
                                currencyClass = it
                            )
                        }
                    },
                    onClose = { showCurrencyDialog = !showCurrencyDialog })
            }
        }
    }
}

@Composable
fun WorldCurrencyChooserContent(
    defaultCurrency: CurrencyClass,
    onSelect: (currencyClass: CurrencyClass) -> Unit,
    onClose: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val list = remember { getCountryCurrencyList() }
    val selectCurrency = remember { mutableStateOf(defaultCurrency) }
    var searchValue by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()

    LaunchedEffect(Unit) {
        if (defaultCurrency == null) return@LaunchedEffect

        coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
            val index = list.indexOfFirst { it.countryName == defaultCurrency.countryName }

            scrollState.scrollToItem(index)
        }
    }

    Card(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Select Currency",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 12.dp)
            )
            val containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceVariant,
                0.5f,
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = searchValue,
                onValueChange = {
                    searchValue = it
                },
                placeholder = {
                    Text(
                        text = "Currency code or name",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                trailingIcon = {
                    if (searchValue.isEmpty()) return@TextField

                    IconButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = { searchValue = "" },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_round_close),
                            contentDescription = null,
                        )
                    }
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                    disabledContainerColor = containerColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { focusManager.clearFocus() }
                ),
            )
            Spacer(Modifier.height(8.dp))
            val filteredList = list
                .filter {
                    it.currencyCode.lowercase().contains(searchValue.lowercase())
                            || it.countryName.lowercase().contains(searchValue.lowercase())
                }

            if (filteredList.isNotEmpty()) {
                Box(Modifier.weight(1F)) {
                    LazyColumn(
                        state = scrollState,
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        filteredList.forEach {
                            itemsCurrency(
                                currencyClass = it,
                                selected = selectCurrency.value.countryName == it.countryName,
                                onClick = {
                                    selectCurrency.value = it
                                },
                            )
                        }
                    }
                }
            } else {
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "currency Not Found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalContentColor.current.copy(alpha = 0.8f),
                    )
                }
            }

            Divider(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
            ) {
                Button(
                    onClick = { onClose() },
                    colors = ButtonDefaults.textButtonColors(),
                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                ) {
                    Text(text = "Cancel")
                }
                Button(
                    onClick = {
                        onSelect(selectCurrency.value!!)
                        onClose()
                    },
                    colors = ButtonDefaults.textButtonColors(),
                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                    enabled = selectCurrency.value !== null,
                ) {
                    Text(text = "Accept")
                }
            }
        }
    }
}

private fun LazyListScope.itemsCurrency(
    currencyClass: CurrencyClass,
    selected: Boolean,
    onClick: () -> Unit,
) {
    item() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = selected,
                    onValueChange = { onClick() },
                    role = Role.Checkbox
                )
                .padding(start = 24.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
        ) {
            Text(
                text = "${currencyClass.flag}   ${currencyClass.countryName}(${currencyClass.currencySymbol})",
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.widthIn(8.dp))
            RadioButton(selected = selected, onClick = null)
        }
    }
}

@Preview(name = "default")
@Composable
private fun PreviewDefault() {
    WorldCurrencyChooserContent(
        defaultCurrency = defaultCurrency,
        onSelect = { },
        onClose = { }
    )
}

@Preview(name = "Night mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNightMode() {
    WorldCurrencyChooserContent(
        defaultCurrency = defaultCurrency,
        onSelect = { },
        onClose = { }
    )
}
//@Composable
//@Preview
//fun SettingsScreenManagePreview() {
//    Surface {
//        SettingsScreenContent()
//    }
//}
//
//@Composable
//@Preview
//fun SettingsScreenCreatePreview() {
//    Surface {
//        SettingsScreenContent()
//    }
//}


