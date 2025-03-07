package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.CustomFonts.numberFont
import com.roaa.expensetracker.Composables.Navigation.Destinations
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.Navigation.handleBackNavigation
import com.roaa.expensetracker.Composables.StatisticsComponent.LineChart
import com.roaa.expensetracker.Composables.color4
import com.roaa.expensetracker.Composables.color8
import com.roaa.expensetracker.Composables.components.ActionConfirmation
import com.roaa.expensetracker.Composables.components.AddPaymentMethodBottomSheet
import com.roaa.expensetracker.Composables.components.BottomSheetContentItemDetails
import com.roaa.expensetracker.Composables.components.ConfirmationAlertDialog
import com.roaa.expensetracker.Composables.components.DropDownBankAccountOption
import com.roaa.expensetracker.Composables.components.EditPaymentMethodBottomSheet
import com.roaa.expensetracker.Composables.components.EmptyScreen
import com.roaa.expensetracker.Composables.components.HomeStatCardSingleNew
import com.roaa.expensetracker.Composables.components.SingleTransaction
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.utils.ColorState
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.Database.Relations.TransactionWithDetails
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.R
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.Constants.INCOME
import com.roaa.expensetracker.Utilities.DeleteAction
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBank
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyCategoryClass
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyTransactionClass
import com.roaa.expensetracker.Utilities.convertDataToSeries
import com.roaa.expensetracker.Utilities.convertTotalExpenseIncomeClassToMap
import com.roaa.expensetracker.Utilities.getCalendarForMonthFromDate
import com.roaa.expensetracker.Utilities.parseAmount
import com.roaa.expensetracker.Utilities.toLong
import com.roaa.expensetracker.ViewModels.BankAccountsViewModel
import com.roaa.expensetracker.ViewModels.PreferencesViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.ViewModels.UiViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun PaymentMethodScreen(
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    modifier: Modifier = Modifier,
    sendUserBack: () -> Unit,
    uiViewModel: UiViewModel,
    bankAccountsViewModel: BankAccountsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    transactionViewModel: TransactionsViewModel = hiltViewModel()
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showEditBottomSheet by remember { mutableStateOf(false) }
    val bankAccountsList by bankAccountsViewModel.getAllBankAccountsExceptCashCompose()
        .collectAsState(
            listOf(emptyBank)
        )

    var actionConfirmationFlag by remember { mutableStateOf(false) }


    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var bankAccountsClass by remember {
        mutableStateOf(
            emptyBank
        )
    }


    BackHandler {
        handleBackNavigation(navigationManager)
    }

    Scaffold(topBar = {
        TopBar(title = "Bank Accounts",
            showDelete = false,
            sendUserBackToPreviousActivity = { handleBackNavigation(navigationManager) },
            delete = {})
    }, floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = {
                showBottomSheet = !showBottomSheet
            },
            icon = { Icon(Icons.Filled.Add, "Localized description") },
            text = { Text(text = "Add Payment Method") },
        )
    }) { paddingValue ->
        if (!bankAccountsList.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(paddingValue)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(bankAccountsList) {
                        PaymentCard(Modifier, it, { bankAccounts ->
                            bankAccountsClass = bankAccounts
                            showEditBottomSheet = !showEditBottomSheet
                        }, { bankAccount ->
                            actionConfirmationFlag = true
                            bankAccountsClass = bankAccount
                        }, { bankAccount ->
                            navigationManager.navigateTo(Destinations.BankDetailsScreen(bankAccount.bankAccountId))
                        })
                    }
                }
            }
        } else {
            EmptyScreen("No Bank Account Found")
        }
    }
    if (showBottomSheet) {
        AddPaymentMethodBottomSheet({ showBottomSheet = !showBottomSheet })
    }
    if (showEditBottomSheet) {
        EditPaymentMethodBottomSheet(
            bankAccountsClass,
            { showEditBottomSheet = !showEditBottomSheet })
    }
    if (actionConfirmationFlag) {
        ActionConfirmation(Modifier,
            bankAccountsList.size > 1,
            bankAccountsClass,
            bankAccountsList,
            { action, targetBankAccountClass ->
                when (action) {
                    DeleteAction.DELETE_BANK_ACCOUNT -> {
                        scope.launch {
                            bankAccountsViewModel.storeBankAccount(bankAccountsClass.apply {
                                this.isActive = false
                            })
                        }
                    }

                    DeleteAction.DELETE_AND_MIGRATE -> {
                        bankAccountsViewModel.migrateTransactions(
                            bankAccountsClass, targetBankAccountClass
                        )
                    }

                    DeleteAction.DELETE_ALL -> {
                        bankAccountsViewModel.deleteBankAccountWithTransactions(bankAccountsClass)
                    }

                }
                actionConfirmationFlag = !actionConfirmationFlag
            }) {
            actionConfirmationFlag = !actionConfirmationFlag
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentCard(
    modifier: Modifier,
    bankAccountsClass: BankAccountsClass,
    editClicked: (bankAccountsClass: BankAccountsClass) -> Unit,
    deleteClicked: (bankAccountClass: BankAccountsClass) -> Unit,
    onSingleItemClick: (bankAccountsClass: BankAccountsClass) -> Unit,
    uiViewModel: UiViewModel = hiltViewModel(),
    bankAccountsViewModel: BankAccountsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    val color = ColorState.fromNumber(bankAccountsClass.cardColorNumber)!!
    val scope = rememberCoroutineScope()
    var showOptionMenu by remember { mutableStateOf(false) }
    val showBottomSheet by remember { mutableStateOf(false) }
    var showConfirmationDeleteDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = modifier
            .padding(0.dp, 4.dp)
            .clip(RoundedCornerShape(25.dp))
            .clickable {
                onSingleItemClick(bankAccountsClass)
            },
        colors = CardDefaults.cardColors(
            containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                color,
                angle = 0.4f,
            )
        )
    ) {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (balanceText, balanceLabel, cardNumber, moreIcon, backgroundImage1, backgroundImage2, progress) = createRefs()
            val circleModifier1 = Modifier.constrainAs(backgroundImage1) {
                end.linkTo(parent.end, margin = 30.dp)
                top.linkTo(parent.top, margin = -30.dp)
            }
            val circleModifier2 = Modifier.constrainAs(backgroundImage2) {
                end.linkTo(parent.end, margin = -50.dp)
            }
            val squareModifier1 = Modifier.constrainAs(backgroundImage1) {
                end.linkTo(parent.end, margin = 30.dp)
                top.linkTo(parent.top, margin = -30.dp)
            }
            val squareModifier2 = Modifier.constrainAs(backgroundImage2) {
                end.linkTo(parent.end, margin = -60.dp)
                bottom.linkTo(parent.bottom, margin = 0.dp)
            }
            val hexagonModifier1 = Modifier.constrainAs(backgroundImage1) {
                end.linkTo(parent.end, margin = -30.dp)
                top.linkTo(parent.top, margin = -60.dp)
            }
            val hexagonModifier2 = Modifier.constrainAs(backgroundImage2) {
                end.linkTo(parent.end, margin = -50.dp)
                bottom.linkTo(parent.bottom, margin = 0.dp)
            }

            val polygonModifier1 = Modifier.constrainAs(backgroundImage1) {
                end.linkTo(parent.end, margin = -30.dp)
                top.linkTo(parent.top, margin = -60.dp)
            }
            val polygonModifier2 = Modifier.constrainAs(backgroundImage2) {
                start.linkTo(parent.start, margin = -20.dp)
                bottom.linkTo(parent.bottom, margin = -70.dp)
            }
            val number =
                if (bankAccountsClass.cardColorNumber % 4 == 0) 4 else bankAccountsClass.cardColorNumber % 4
            val (image, modifier1, modifier2) = when (number) {
                1 -> Triple(
                    rememberAsyncImagePainter(R.drawable.shape_soft_circle),
                    circleModifier1,
                    circleModifier2
                )

                2 -> Triple(
                    rememberAsyncImagePainter(R.drawable.shape_soft_square),
                    squareModifier1,
                    squareModifier2
                )

                3 -> Triple(
                    rememberAsyncImagePainter(R.drawable.shape_soft_hexagon),
                    hexagonModifier1,
                    hexagonModifier1
                )

                4 -> Triple(
                    rememberAsyncImagePainter(
                        R.drawable.shape_soft_polygon,
                    ),
                    polygonModifier1,
                    polygonModifier2
                )

                else -> Triple(
                    rememberAsyncImagePainter(R.drawable.shape_soft_circle),
                    circleModifier1,
                    circleModifier2
                )
            }

            Text(text = "₹${bankAccountsClass.currentAmount}",
                style = typography.headlineMedium.copy(fontFamily = numberFont),
                modifier = Modifier.constrainAs(balanceText) {
                    top.linkTo(parent.top, margin = 24.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                })

            Text(text = "Amount",
                style = typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
                modifier = Modifier.constrainAs(balanceLabel) {
                    top.linkTo(balanceText.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                })

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(cardNumber) {
                    top.linkTo(progress.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                    bottom.linkTo(parent.bottom, margin = 24.dp)
                },
                horizontalArrangement = Arrangement.Center
            ) {


                Surface(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .size(36.dp)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        val image = rememberAsyncImagePainter(IconState.fromNumber(24))
                        Image(
                            painter = image,
                            contentDescription = "Test Image",
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }


                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth(0.80f)

                ) {
                    if (true) {
                        Text(
                            text = bankAccountsClass.bankName,
                            style = typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        )
                    }
                }


            }


            LinearProgressIndicator(
                progress = { Math.random().toFloat() },
                modifier = Modifier
                    .height(30.dp)
                    .constrainAs(progress) {
                        top.linkTo(balanceLabel.bottom, 20.dp)
                        start.linkTo(parent.start, 24.dp)
                        end.linkTo(parent.end, 24.dp)
                        width = Dimension.fillToConstraints
                    },
                color = color.copy(alpha = 0.50f),
                trackColor = color.copy(alpha = 0.10f),
                strokeCap = StrokeCap.Round,
                gapSize = -30.dp,
            )
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = modifier1
                    .size(144.dp),
                alpha = 0.1f,
                colorFilter = ColorFilter.tint(color)
            )
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = modifier2
                    .size(144.dp),
                alpha = 0.1f,
                colorFilter = ColorFilter.tint(color)
            )
            Box(Modifier.constrainAs(moreIcon) {
                top.linkTo(parent.top, 18.dp)
                end.linkTo(parent.end, 18.dp)
            }) {
                IconButton(
                    onClick = { showOptionMenu = !showOptionMenu },
                ) {
                    Icon(
                        Icons.Filled.MoreVert, contentDescription = null,
                    )
                }
                val colorPallet =
                    toPalette(ColorState.fromNumber(bankAccountsClass.cardColorNumber)!!)
                if (showOptionMenu) DropDownBankAccountOption(menuExpanded = showOptionMenu,
                    colorPallet = colorPallet,
                    { showOptionMenu = false },
                    onPrimaryClicked = {
                        scope.launch {
                            preferencesViewModel.setPrimaryAccountNumber(
                                bankAccountsClass.bankAccountId
                            )
                        }
                        scope.launch { preferencesViewModel.setPrimaryAccount(bankAccountsClass) }
                        showOptionMenu = false
                    },
                    editClicked = {
                        editClicked(bankAccountsClass)
                    },
                    deleteClicked = {
                        deleteClicked(bankAccountsClass)
                        //showConfirmationDeleteDialog = !showConfirmationDeleteDialog
                    })
            }
        }
    }
    //need to remove this changed the flow
    if (showConfirmationDeleteDialog) ConfirmationAlertDialog(
        onDismissRequest = { showConfirmationDeleteDialog = !showConfirmationDeleteDialog },
        onConfirmation = {
            bankAccountsViewModel.deleteBankAccount(bankAccountsClass)
            showConfirmationDeleteDialog = !showConfirmationDeleteDialog
        },
        dialogTitle = "Confirm Delete?",
        dialogText = "Are you sure, that you want to delete this bank account?",
        icon = ImageVector.vectorResource(R.drawable.icon_expense)
    )

}

@Composable
fun PaymentMethodCardOld() {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(16.dp, 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                //  onSingleItemClick(item)
            },
        colors = CardDefaults.cardColors(
            containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                orange,
                angle = 0.1f,
            )
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp, 18.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .size(56.dp)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    val image = rememberAsyncImagePainter(IconState.fromNumber(25))
                    Image(
                        painter = image,
                        contentDescription = "Test Image",
                        modifier = Modifier.size(36.dp),
                    )
                }
            }


            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(0.80f)

            ) {
                Text(
                    text = "Cash",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = typography.titleMedium.copy(
                        fontFamily = numberFont, fontWeight = FontWeight.SemiBold
                    ),
                )
                if (true) {
                    Text(
                        text = "HDFC Bank",
                        style = typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = { }, modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.Filled.MoreVert, contentDescription = null,


                    )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivePaymentCard(
    color: Color,
    amount: String,
    bankName: String,
) {

    val newColor = combineColors(
        MaterialTheme.colorScheme.surface,
        color,
        angle = 0.4f,
    )
    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier
            .padding(16.dp, 4.dp)
            .clip(RoundedCornerShape(25.dp)),
        colors = CardDefaults.cardColors(
            containerColor = newColor
        )
    ) {

        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (balanceText, balanceLabel, cardNumber, moreIcon, backgroundImage1, backgroundImage2) = createRefs()

            val circleModifier1 = Modifier.constrainAs(backgroundImage1) {
                end.linkTo(parent.end, margin = 30.dp)
                top.linkTo(parent.top, margin = -30.dp)
            }
            val circleModifier2 = Modifier.constrainAs(backgroundImage2) {
                end.linkTo(parent.end, margin = -50.dp)
            }
            val squareModifier1 = Modifier.constrainAs(backgroundImage1) {
                end.linkTo(parent.end, margin = 30.dp)
                top.linkTo(parent.top, margin = -30.dp)
            }
            val squareModifier2 = Modifier.constrainAs(backgroundImage2) {
                end.linkTo(parent.end, margin = -60.dp)
                bottom.linkTo(parent.bottom, margin = 0.dp)
            }
            val hexagonModifier1 = Modifier.constrainAs(backgroundImage1) {
                end.linkTo(parent.end, margin = -30.dp)
                top.linkTo(parent.top, margin = -60.dp)
            }
            val hexagonModifier2 = Modifier.constrainAs(backgroundImage2) {
                end.linkTo(parent.end, margin = -50.dp)
                bottom.linkTo(parent.bottom, margin = 0.dp)
            }

            val polygonModifier1 = Modifier.constrainAs(backgroundImage1) {
                end.linkTo(parent.end, margin = -30.dp)
                top.linkTo(parent.top, margin = -60.dp)
            }
            val polygonModifier2 = Modifier.constrainAs(backgroundImage2) {
                start.linkTo(parent.start, margin = -20.dp)
                bottom.linkTo(parent.bottom, margin = -70.dp)
            }
            val number =
                if (ColorState.fromColor(color) % 4 == 0) 4 else ColorState.fromColor(color) % 4
            val (image, modifier1, modifier2) = when (number) {
                1 -> Triple(
                    rememberAsyncImagePainter(R.drawable.shape_soft_circle),
                    circleModifier1,
                    circleModifier2
                )

                2 -> Triple(
                    rememberAsyncImagePainter(R.drawable.shape_soft_square),
                    squareModifier1,
                    squareModifier2
                )

                3 -> Triple(
                    rememberAsyncImagePainter(R.drawable.shape_soft_hexagon),
                    hexagonModifier1,
                    hexagonModifier1
                )

                4 -> Triple(
                    rememberAsyncImagePainter(
                        R.drawable.shape_soft_polygon,
                        transform = AsyncImagePainter.DefaultTransform
                    ),
                    polygonModifier1,
                    polygonModifier2
                )

                else -> Triple(
                    rememberAsyncImagePainter(R.drawable.shape_soft_circle),
                    circleModifier1,
                    circleModifier2
                )
            }


            Row(modifier = Modifier.constrainAs(balanceText) {
                top.linkTo(parent.top, margin = 24.dp)
                start.linkTo(parent.start, margin = 24.dp)
            }, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "₹",
                    style = typography.headlineMedium.copy(
                        fontFamily = numberFont,
                    ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = amount, style = typography.headlineMedium.copy(fontFamily = numberFont))
            }
            Text(text = "Amount",
                style = typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
                modifier = Modifier.constrainAs(balanceLabel) {
                    top.linkTo(balanceText.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                })

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(cardNumber) {
                    top.linkTo(balanceLabel.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                    bottom.linkTo(parent.bottom, margin = 24.dp)
                },
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .size(56.dp)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        val image = rememberAsyncImagePainter(IconState.fromNumber(24))
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
                        .fillMaxWidth(0.80f)

                ) {

                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = bankName, style = typography.titleLarge.copy(fontFamily = numberFont)
                    )

                }


            }

            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = modifier1
                    .size(144.dp)
                    .zIndex(1f),
                alpha = 0.1f,
                colorFilter = ColorFilter.tint(color.copy())
            )
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = modifier2
                    .size(144.dp)
                    .zIndex(1f),
                alpha = 0.1f,
                colorFilter = ColorFilter.tint(color)
            )
        }
    }
}


// payment detail screen
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PaymentDetailsScreen(
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    modifier: Modifier = Modifier,
    bankAccountId: Long,
    uiViewModel: UiViewModel,
    bankAccountsViewModel: BankAccountsViewModel = hiltViewModel(),
    transactionViewModel: TransactionsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    val bankAccount by bankAccountsViewModel.getSingleBankAccountForCompose(bankAccountId)
        .collectAsState(emptyBank)
    val showExperimentalComponent by preferencesViewModel.showExperimentalComponent.collectAsState(
        false
    )
    var showEditBottomSheet by remember { mutableStateOf(false) }
    var actionConfirmationFlag by remember { mutableStateOf(false) }
    var bankAccountsClass by remember {
        mutableStateOf(
            emptyBank
        )
    }
    val transactionListForBankAccount by transactionViewModel.getTransactionsListForBankAccountId(
        bankAccountId
    ).collectAsState(
        listOf()
    )
    val bankAccountsList by bankAccountsViewModel.getAllBankAccountsExceptCashCompose()
        .collectAsState(
            listOf(emptyBank)
        )
    val scope = rememberCoroutineScope()
    val currentMonthStart = LocalDate.now()
    val allDays = remember(currentMonthStart) {
        getCalendarForMonthFromDate(currentMonthStart)
    }


    val totalExpenseListFromRoom by transactionViewModel.getListOfTotalAmountPerDayForRangeForComposeForBankAccountId(
        allDays[0].toLong(),
        allDays[41].toLong(),
        bankAccountId
    ).collectAsState(listOf())

    val totalValuesPerDayForMonthMap = remember(totalExpenseListFromRoom) {
        convertTotalExpenseIncomeClassToMap(totalExpenseListFromRoom)
    }

    var totalExpense by remember { mutableFloatStateOf(0f) }
    var totalIncome by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(totalExpenseListFromRoom) {
        totalExpenseListFromRoom.forEach {
            totalExpense += it.totalExpense
            totalIncome += it.totalIncome
        }
    }
    var bottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var singleTransaction by remember {
        mutableStateOf(
            TransactionWithDetails(
                emptyTransactionClass,
                emptyCategoryClass,
                emptyBank
            )
        )
    }
    val (expenseListPerDayHashMap, incomeListPerDayHashMap) =
        convertDataToSeries(
            allDays,
            totalValuesPerDayForMonthMap
        )


    BackHandler { handleBackNavigation(navigationManager) }

    Scaffold(topBar = {
        TopBar(title = "",
            showDelete = false,
            sendUserBackToPreviousActivity = { handleBackNavigation(navigationManager) },
            delete = {})
    }) {
        val modifierWithHorizontalPadding = Modifier.padding(16.dp, 0.dp)
        Column(
            Modifier
                .padding(it)
                .padding()
        ) {
            Column {

                LazyColumn {
                    item {
                        PaymentCard(modifier = modifierWithHorizontalPadding,
                            bankAccountsClass = bankAccount,
                            editClicked = { bankAccounts ->
                                bankAccountsClass = bankAccounts
                                showEditBottomSheet = !showEditBottomSheet
                            },
                            deleteClicked = { bankAccount ->
                                actionConfirmationFlag = true
                                bankAccountsClass = bankAccount
                            },
                            onSingleItemClick = {})
                        Spacer(Modifier.height(16.dp))
                        Text(
                            modifier = modifierWithHorizontalPadding.padding(vertical = 8.dp),
                            text = "Card Statistics",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Card(
                            modifier = modifierWithHorizontalPadding,
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Row(
                                Modifier
                            ) {
                                HomeStatCardSingleNew(
                                    Modifier
                                        .weight(1f)
                                        .wrapContentHeight(),
                                    "₹ ${parseAmount(totalExpense)}",
                                    EXPENSE
                                )
                                HomeStatCardSingleNew(
                                    Modifier
                                        .weight(1f)
                                        .wrapContentHeight(),
                                    "₹ ${parseAmount(totalIncome)}",
                                    INCOME
                                )
                                HomeStatCardSingleNew(
                                    Modifier
                                        .weight(1f)
                                        .wrapContentHeight(),
                                    "${transactionListForBankAccount.size}",
                                    "Transactions"
                                )
                            }
                        }
                        Column(
                            Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                SingleInfoBox(
                                    Modifier.weight(1f),
                                    "Total Expense",
                                    "₹ ${parseAmount(totalExpense)}",
                                )
                                SingleInfoBox(
                                    Modifier.weight(1f),
                                    "Maximum Spend",
                                    "₹ 3000",
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                SingleInfoBox(
                                    Modifier.weight(1f),
                                    "Minimum Spend",
                                    "₹ 3000",
                                )
                                SingleInfoBox(
                                    Modifier.weight(1f),
                                    "Maximum Spend",
                                    "₹ 3000",
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                SingleInfoBox(
                                    Modifier.weight(1f),
                                    "Total Transactions",
                                    "39",
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                    item {
                        if (transactionListForBankAccount.isNotEmpty()) {
                            Text(
                                modifier = modifierWithHorizontalPadding.padding(vertical = 8.dp),
                                text = "Transactions",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    items(transactionListForBankAccount, key = { it.transaction.id }) {
                        SingleTransaction(it) {
                            singleTransaction = it
                            bottomSheet = !bottomSheet
                        }
                    }
                }
            }
        }
        if (bottomSheet) {
            BottomSheetContentItemDetails(
                bottomSheetState,
                singleTransaction,
                { bottomSheet = !bottomSheet })
        }
        if (showEditBottomSheet) {
            EditPaymentMethodBottomSheet(
                bankAccountsClass,
                { showEditBottomSheet = !showEditBottomSheet })
        }
        if (actionConfirmationFlag) {
            ActionConfirmation(Modifier,
                bankAccountsList.size > 1,
                bankAccountsClass,
                bankAccountsList,
                { action, targetBankAccountClass ->
                    when (action) {
                        DeleteAction.DELETE_BANK_ACCOUNT -> {
                            scope.launch {
                                bankAccountsViewModel.storeBankAccount(bankAccountsClass.apply {
                                    this.isActive = false
                                })
                            }
                        }

                        DeleteAction.DELETE_AND_MIGRATE -> {
                            bankAccountsViewModel.migrateTransactions(
                                bankAccountsClass, targetBankAccountClass
                            )
                        }

                        DeleteAction.DELETE_ALL -> {
                            bankAccountsViewModel.deleteBankAccountWithTransactions(
                                bankAccountsClass
                            )
                        }

                    }
                    actionConfirmationFlag = !actionConfirmationFlag
                    handleBackNavigation(navigationManager)
                }) {
                actionConfirmationFlag = !actionConfirmationFlag
            }
        }
    }
}

@Composable
fun StatisticsCardForCardStats(
    modifier: Modifier = Modifier,
    expenseListPerDay: LinkedHashMap<String, Int>,
    incomeListPerDay: LinkedHashMap<String, Int>,
    type: String,
    totalExpense: Float,
    totalIncome: Float,
) {
    val subTitle = if (type == EXPENSE) "Total Expense" else "Total Income"
    val title = parseAmount(if (type == EXPENSE) totalExpense else totalIncome)
    val palette = toPalette(if (type == EXPENSE) color8 else color4)
    val cardColor = combineColors(
        MaterialTheme.colorScheme.surface,
        palette.container,
        angle = 0.5f,
    )
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(25.dp)
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subTitle,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(8.dp))
            Box() {
                if (type == EXPENSE) {
                    LineChart(
                        Modifier,
                        palette,
                        expenseListPerDay,
                    )
                } else {
                    LineChart(
                        Modifier,
                        palette,
                        incomeListPerDay,
                    )
                }
            }
        }
    }
}



