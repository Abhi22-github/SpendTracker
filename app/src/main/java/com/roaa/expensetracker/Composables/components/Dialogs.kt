package com.roaa.expensetracker.Composables.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowRight
import androidx.compose.material.icons.rounded.Transform
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.roaa.expensetracker.Composables.Screens.startEndPadding
import com.roaa.expensetracker.Composables.utils.toPalette
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.R
import com.roaa.expensetracker.Utilities.DeleteAction
import com.roaa.expensetracker.Utilities.UtilityModalClass.emptyBank
import com.roaa.expensetracker.Utilities.deleteActionList


@Composable
fun EmptyScreen(text: String = "No Transactions found") {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_screen_animation))

    // Box for centering content on the screen
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Display the Lottie animation
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LottieAnimation(composition = composition, modifier = Modifier.wrapContentSize())
            Text(
                text = text, style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
fun ConfirmationAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(icon = {
        Icon(icon, contentDescription = "Example Icon")
    }, title = {
        Text(text = dialogTitle)
    }, text = {
        Text(text = dialogText)
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onConfirmation()
        }) {
            Text("Confirm")
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text("Cancel")
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionConfirmation(
    modifier: Modifier = Modifier,
    shouldEnableTheMigration: Boolean,
    bankAccountsClass: BankAccountsClass,
    bankAccountList: List<BankAccountsClass>,
    performAction: (deleteAction: DeleteAction, bankAccount: BankAccountsClass) -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf(DeleteAction.DELETE_BANK_ACCOUNT) }
    var filteredBankAccountList = remember { bankAccountList }
    var selectedBankAccountForMigration by remember {
        mutableStateOf(emptyBank)
    }
    if (shouldEnableTheMigration) {
        filteredBankAccountList =
            bankAccountList.filter { it.bankAccountId != bankAccountsClass.bankAccountId }
        selectedBankAccountForMigration = filteredBankAccountList[0]
    }

    ModalBottomSheet(onDismissRequest = {
        onDismissRequest()
    },
        sheetState = sheetState,
        modifier = Modifier
            .imePadding()
            .fillMaxWidth(),
        contentWindowInsets = { WindowInsets.ime }) {
        ActionConfirmationBottomSheetContent(Modifier,
            selectedItem,
            shouldEnableTheMigration,
            filteredBankAccountList,
            selectedBankAccountForMigration,
            { selectedBankAccountForMigration = it },
            { selectedItem = it }) { performAction(selectedItem, selectedBankAccountForMigration) }
    }
}

@Composable
fun ActionConfirmationBottomSheetContent(
    modifier: Modifier = Modifier,
    selectedItem: DeleteAction,
    shouldEnableTheMigration: Boolean,
    bankAccountList: List<BankAccountsClass>,
    selectedBankAccountForMigration: BankAccountsClass,
    setSelectedBankAccountForMigration: (BankAccountsClass) -> Unit,
    setSelectedItem: (DeleteAction) -> Unit,
    confirmButtonClick: () -> Unit,
) {

    val modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)

    Column(
        Modifier
            .navigationBarsPadding()
            .padding(bottom = 8.dp)
    ) {
        deleteActionList.forEach {
            SingleActionItem(modifier,
                it.header,
                it.body,
                it.action == selectedItem,
                it.action,
                shouldEnableTheMigration,
                bankAccountList,
                selectedBankAccountForMigration,
                { setSelectedBankAccountForMigration(it) },
                { setSelectedItem(it) })
        }
        FilledTonalButton(
            onClick = { confirmButtonClick() },
            modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.primary),

            ) {
            Text(text = "Confirm", color = MaterialTheme.colorScheme.onPrimary)
        }

    }
}

@Composable
fun SingleActionItem(
    modifier: Modifier = Modifier,
    headerText: String,
    bodyText: String,
    isSelected: Boolean,
    action: DeleteAction,
    shouldEnableTheMigration: Boolean,
    bankAccountList: List<BankAccountsClass>,
    selectedBankAccountForMigration: BankAccountsClass,
    setSelectedBankAccountForMigration: (BankAccountsClass) -> Unit,
    actionSetter: (DeleteAction) -> Unit
) {
    val icon = when (action) {
        DeleteAction.DELETE_BANK_ACCOUNT -> {
            Icons.Rounded.Delete
        }

        DeleteAction.DELETE_AND_MIGRATE -> {
            Icons.Rounded.Transform
        }

        DeleteAction.DELETE_ALL -> {
            Icons.Rounded.DeleteForever
        }
    }
    val cardColor by animateColorAsState(
        if (!shouldEnableTheMigration && action == DeleteAction.DELETE_AND_MIGRATE) {
            MaterialTheme.colorScheme.surfaceContainerHigh
        } else if (isSelected) MaterialTheme.colorScheme.secondaryContainer.copy(0.5f) else MaterialTheme.colorScheme.surfaceContainerLow
    )
    val textColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
    )
    val borderColor by animateColorAsState(
        if (isSelected) Color.Transparent else MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.1f
        )
    )
    val borderWidth by animateDpAsState(
        if (isSelected) 0.dp else 0.1.dp
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        ),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(enabled = if (action == DeleteAction.DELETE_AND_MIGRATE) if (shouldEnableTheMigration) true else false else true) {
                actionSetter(
                    action
                )
            },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(borderWidth, borderColor),

        ) {
        Row(
            modifier = Modifier.padding(startEndPadding, 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.padding(10.dp))
            Spacer(Modifier.width(5.dp))
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = bodyText,
                    style = MaterialTheme.typography.labelLarge,
                    color = textColor.copy(0.6f),
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedVisibility(isSelected && action == DeleteAction.DELETE_AND_MIGRATE) {
                    var bankAccountMenuExpanded by remember { mutableStateOf(false) }
                    val colorPalette = toPalette(MaterialTheme.colorScheme.primary)
                    Box() {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Icon(Icons.Rounded.KeyboardDoubleArrowRight, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            MigrationSelections(Modifier, selectedBankAccountForMigration) {
                                bankAccountMenuExpanded = !bankAccountMenuExpanded
                            }
                        }
                        DropDownMenuForBankAccounts(bankAccountMenuExpanded,
                            colorPalette,
                            onDismiss = { bankAccountMenuExpanded = false },
                            bankAccountList,
                            selectedBankAccountSetter = {
                                setSelectedBankAccountForMigration(it)
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun MigrationSelections(
    modifier: Modifier = Modifier, selectedBankAccount: BankAccountsClass, openDropDown: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { openDropDown() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedBankAccount.bankName, modifier = Modifier.weight(0.8f))
            Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ActionConfirmationPreview() {

    ActionConfirmationBottomSheetContent(modifier = Modifier.padding(16.dp, 16.dp),
        selectedItem = DeleteAction.DELETE_AND_MIGRATE,
        shouldEnableTheMigration = true,
        bankAccountList = listOf<BankAccountsClass>(),
        selectedBankAccountForMigration = emptyBank,
        setSelectedBankAccountForMigration = {},
        setSelectedItem = { },
        confirmButtonClick = {})
}

@Preview
@Composable
private fun MigrationSelectionsPreview() {
    MigrationSelections(Modifier, emptyBank, {})
}

//@Preview
//@Composable
//fun AlertDialogPreview() {
//    ExpenseTrackerTheme {
//        Surface {
//            ConfirmationAlertDialog(
//                {},
//                {},
//                "Change Budget",
//                "Are you sure, you want to change the current budget?",
//                ImageVector.vectorResource(R.drawable.icon_expense)
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//fun EmptyScreenPreview() {
//    ExpenseTrackerTheme {
//        EmptyScreen()
//    }
//}