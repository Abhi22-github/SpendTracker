package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.CustomFonts.numberFont
import com.roaa.expensetracker.Composables.Navigation.Destinations
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.components.ActionConfirmation
import com.roaa.expensetracker.Composables.components.AddPaymentMethodBottomSheet
import com.roaa.expensetracker.Composables.components.ConfirmationAlertDialog
import com.roaa.expensetracker.Composables.components.DropDownBankAccountOption
import com.roaa.expensetracker.Composables.components.EmptyScreen
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
import com.roaa.expensetracker.Utilities.Constants.CASH
import com.roaa.expensetracker.Utilities.DeleteAction
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBank
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyTransactionWithDetailsClass
import com.roaa.expensetracker.Utilities.extractNumbers
import com.roaa.expensetracker.ViewModels.BankAccountsViewModel
import com.roaa.expensetracker.ViewModels.PreferencesViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.ViewModels.UiViewModel
import kotlinx.coroutines.launch

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun PaymentMethodScreen(
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    modifier: Modifier = Modifier,
    sendUserBack: () -> Unit,
    uiViewModel: UiViewModel = hiltViewModel(),
    bankAccountsViewModel: BankAccountsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    transactionViewModel: TransactionsViewModel = hiltViewModel()
) {
    val showBottomSheet by uiViewModel.paymentMethodBottomSheetStatus.collectAsState()
    val bankAccountsList by bankAccountsViewModel.getAllBankAccountsExceptCashCompose()
        .collectAsState(
            listOf(emptyBank)
        )
    val allTransaction by transactionViewModel.allTransactions.collectAsState(
        listOf(emptyTransactionWithDetailsClass)
    )
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { bankAccountsList.size })
    var actionConfirmationFlag by remember { mutableStateOf(false) }

    var filteredTransactionList = remember { emptyList<TransactionWithDetails>() }

    LaunchedEffect(pagerState.currentPage) {
        Log.d(
            "PaymentMethodScreen",
            "${pagerState.currentPage} ${filteredTransactionList.size} ${bankAccountsList.size} ${allTransaction.size}"
        )
//        if(bankAccountsList.isNotEmpty()) {
//            filteredTransactionList = allTransaction.filter {
//                it.BankAccount.bankAccountId == bankAccountsList.get(
//                    pagerState.currentPage
//                ).bankAccountId
//            }
//        }
    }


    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    //empty bank account class
    val emptyBankAccountsClass = BankAccountsClass(
        bankAccountId = 0L,
        initialAmount = 0,
        currentAmount = 0,
        bankName = "",
        cardColorNumber = 1,
        cardIconNumber = 25,
        accountType = CASH,
        isActive = true
    )

    var bankAccountsClass by remember {
        mutableStateOf(
            emptyBankAccountsClass
        )
    }

    // Function to handle back navigation logic
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

    Scaffold(topBar = {
        TopBar(title = "Bank Accounts",
            showDelete = false,
            sendUserBackToPreviousActivity = { handleBackNavigation() },
            delete = {})
    }, floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = {
                scope.launch {
                    uiViewModel.paymentMethodBottomSheetStatus.emit(true)
                }
            },
            icon = { Icon(Icons.Filled.Add, "Localized description") },
            text = { Text(text = "Add Payment Method") },
        )
    }) { paddingValue ->
        if (!bankAccountsList.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(paddingValue)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    pageSpacing = 8.dp
                ) { page ->
                    filteredTransactionList = allTransaction.filter {
                        it.BankAccount.bankAccountId == bankAccountsList.getOrElse(
                            pagerState.currentPage
                        ) { emptyBankAccountsClass }.bankAccountId
                    }
                    Column {
                        PaymentCard(Modifier,
                            bankAccountsList.get(page),
                            bankAccountsViewModel,
                            { bankAccounts ->
                                bankAccountsClass = bankAccounts
                                scope.launch {
                                    uiViewModel.paymentMethodBottomSheetStatus.emit(true)
                                }
                            },
                            { bankAccount ->
                                actionConfirmationFlag = true
                                bankAccountsClass = bankAccount
                            })
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pagerState.pageCount) { index ->
                        val isSelected = pagerState.currentPage == index
                        val dotSize by animateDpAsState(
                            targetValue = if (isSelected) 14.dp else 8.dp,
                            animationSpec = tween(durationMillis = 300),
                            label = "Dot Size"
                        )

                        val dotColor by animateColorAsState(
                            targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest,
                            animationSpec = tween(durationMillis = 300),
                            label = "Dot Color"
                        )

                        Box(
                            modifier = Modifier
                                .width(dotSize)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(dotColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxSize()

                ) {
                    Text(
                        text = "Transactions",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    LazyColumn {
                        items(filteredTransactionList, key = { it.transaction.id }) {
                            SingleTransaction(it) { }
                        }
                    }
                }
            }
        } else {
            EmptyScreen("No Bank Account Found")
        }
    }
    if (showBottomSheet) {
        AddPaymentMethodBottomSheet(bankAccountsClass)
        bankAccountsClass = emptyBankAccountsClass
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
    bankAccountsViewModel: BankAccountsViewModel,
    editClicked: (bankAccountsClass: BankAccountsClass) -> Unit,
    deleteClicked: (bankAccountClass: BankAccountsClass) -> Unit,
    uiViewModel: UiViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    val color = ColorState.fromNumber(bankAccountsClass.cardColorNumber)!!
    val scope = rememberCoroutineScope()
    var showOptionMenu by remember { mutableStateOf(false) }
    val showBottomSheet by uiViewModel.paymentMethodBottomSheetStatus.collectAsState()
    var showConfirmationDeleteDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = modifier
            .padding(0.dp, 4.dp)
            .clip(RoundedCornerShape(25.dp))
            .clickable {
                //  onSingleItemClick(item)
            },
        colors = CardDefaults.cardColors(
            containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                color,
                angle = 0.3f,
            )
        )
    ) {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (balanceText, balanceLabel, cardNumber, moreIcon, backgroundImage1, backgroundImage2, progress) = createRefs()

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

            val image = rememberAsyncImagePainter(R.drawable.shape_soft_star_1)
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(144.dp)
                    .constrainAs(backgroundImage1) {
                        top.linkTo(parent.top, margin = -30.dp)
                        start.linkTo(parent.start, margin = -30.dp)
                    },
                alpha = 0.1f,
                colorFilter = ColorFilter.tint(color)
            )
            LinearProgressIndicator(progress = { Math.random().toFloat() },
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
                })
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(144.dp)
                    .constrainAs(backgroundImage2) {
                        end.linkTo(parent.end, margin = -30.dp)
                        bottom.linkTo(parent.bottom, margin = -30.dp)
                    },
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
    bankAccountsClass: BankAccountsClass,
    sendBankAmount: (String) -> Unit,
    sendBankName: (String) -> Unit,
    uiViewModel: UiViewModel = hiltViewModel(),
) {

    var bankAmount by remember { mutableStateOf(TextFieldValue(bankAccountsClass.currentAmount.toString())) }
    var bankName by remember { mutableStateOf(TextFieldValue(bankAccountsClass.bankName)) }
    var iconToggle by remember { mutableStateOf(false) }

    val hintStyleAmount = typography.titleLarge.copy(fontFamily = numberFont)
    val hintColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)

    val newColor = combineColors(
        MaterialTheme.colorScheme.surface,
        color,
        angle = 0.1f,
    )
    val scope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(16.dp, 4.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = newColor
        )
    ) {

        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (balanceText, balanceLabel, cardNumber, moreIcon, backgroundImage1, backgroundImage2) = createRefs()

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
                Box {
                    BasicTextField(value = bankAmount,
                        onValueChange = {
                            bankAmount = TextFieldValue(
                                extractNumbers(it.text).toString(),
                                selection = TextRange(extractNumbers(it.text).toString().length)
                            )
                            sendBankAmount(bankAmount.text)
                            scope.launch { uiViewModel.errorStatusInBankAccountAdd.emit(false) }
                        },
                        cursorBrush = SolidColor(color),
                        modifier = Modifier
                            .background(
                                color.copy(alpha = 0.2f), RoundedCornerShape(5.dp)
                            )
                            .padding(10.dp, 3.dp),
                        textStyle = typography.titleLarge.copy(fontFamily = numberFont),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        decorationBox = { innerTextField ->
                            if (bankAmount.text.isEmpty()) {
                                Text(
                                    text = "Amount",
                                    style = hintStyleAmount,
                                    color = hintColor,
                                )
                            }
                            innerTextField()
                        })
                }
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
                    shape = RoundedCornerShape(12.dp),
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
                    BasicTextField(
                        value = bankName,
                        onValueChange = {
                            bankName = it
                            sendBankName(bankName.text)
                            scope.launch { uiViewModel.errorStatusInBankAccountAdd.emit(false) }
                        },
                        cursorBrush = SolidColor(color),
                        modifier = Modifier
                            .background(
                                color.copy(alpha = 0.2f), RoundedCornerShape(5.dp)
                            )
                            .padding(10.dp, 3.dp),
                        textStyle = typography.titleLarge.copy(fontFamily = numberFont),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
                    ) { innerTextField ->
                        if (bankName.text.isEmpty()) {
                            Text(
                                text = "Bank Name",
                                style = hintStyleAmount.copy(),
                                color = hintColor,
                            )
                        }
                        innerTextField()

                    }
//                    Text(
//                        text = liveBankName,
//                        style = typography.labelLarge.copy(fontWeight = FontWeight.Medium),
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
//                    )

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
                    },
                alpha = 0.1f,
                colorFilter = ColorFilter.tint(color)
            )
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(144.dp)
                    .constrainAs(backgroundImage2) {
                        end.linkTo(parent.end, margin = -30.dp)
                        bottom.linkTo(parent.bottom, margin = -30.dp)
                    },
                alpha = 0.1f,
                colorFilter = ColorFilter.tint(color)
            )
            var icon = Icons.TwoTone.Star
            if (iconToggle) icon = Icons.Filled.Star
            else icon = Icons.TwoTone.Star

            IconButton(onClick = { iconToggle = !iconToggle },
                modifier = Modifier.constrainAs(moreIcon) {
                    top.linkTo(parent.top, 18.dp)
                    end.linkTo(parent.end, 18.dp)
                }) {
                Icon(
                    imageVector = icon, contentDescription = null, tint = color
                )
            }
        }
    }
}


