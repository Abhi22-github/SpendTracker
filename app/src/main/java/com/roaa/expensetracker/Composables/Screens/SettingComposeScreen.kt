package com.roaa.expensetracker.Composables.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.roaa.expensetracker.Composables.ThemeMode
import com.roaa.expensetracker.Composables.components.BudgetBottomSheet
import com.roaa.expensetracker.Composables.components.SingleItemRadioButton
import com.roaa.expensetracker.Composables.components.SpendsBudgetCard
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.ViewModels.PreferencesViewModel
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
    sendUserBack: () -> Unit,
    preferenceViewModel: PreferencesViewModel = hiltViewModel()
) {
    val isBudgetSet by preferenceViewModel.isBudgetSet.collectAsState(false)
    val budget by preferenceViewModel.getBudgetValue.collectAsState(0f)
    SettingsScreenContent(sendUserBack, isBudgetSet, budget)
}

val startEndPadding = 16.dp
val topBottomPadding = 0.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    sendUserBack: () -> Unit,
    isBudgetSet: Boolean,
    budget: Float?,
    preferenceViewModel: PreferencesViewModel = hiltViewModel()
) {
    val themeSelected by preferenceViewModel.getThemeMode.collectAsState(ThemeMode.SYSTEM.toString())
    val showForecast by preferenceViewModel.showForecastBar.collectAsState(false)
    val sheetState = rememberModalBottomSheetState()
    var bottomSheet by remember { mutableStateOf(false) }
    val list = listOf("LIGHT", "NIGHT", "SYSTEM")
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    Column {
        TopBar(title = "Settings",false,{sendUserBack()},{})
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
                        selectedItem = { preferenceViewModel.saveTheme(it) },
                        themeSelected
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
            Row() {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Forecast Budget", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(startEndPadding, topBottomPadding)
                    )
                    Text(
                        text = "Show Forecast budget bar while Adding the transactions",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
                        modifier = Modifier.padding(startEndPadding, topBottomPadding)
                    )
                }
                Switch(
                    checked = showForecast,
                    onCheckedChange = {
                        scope.launch {
                            preferenceViewModel.setForecastState(it)
                        }
                    },
                    modifier = Modifier
                        .weight(.3f)
                        .align(Alignment.CenterVertically)
                )
            }

            Spacer(Modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .padding(startEndPadding, 4.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Budget", style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Please set up a budget accordingly with custom time frame so that we can better analyze it accordingly",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
                )
                Box(
                    modifier = Modifier
                        .height(128.dp)
                        .padding(top = 10.dp)
                ) {
                    key(budget) {
                        budget?.let {
                            SpendsBudgetCard(
                                spend = it,
                                budget = 1000f,
                            )
                        }
                    }

                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (!isBudgetSet) {
                        FilledTonalButton(
                            onClick = { bottomSheet = true },
                            colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                MaterialTheme.colorScheme.onSecondaryContainer,
                                MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        ) {
                            Text(
                                text = "Create Budget",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    } else {
                        FilledTonalButton(
                            onClick = { bottomSheet = true },
                            colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                MaterialTheme.colorScheme.onSecondaryContainer,
                                MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        ) {
                            Text(
                                text = "Manage Budget",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
    if (bottomSheet) {
        if (isBudgetSet) {
            var amountText by remember { mutableStateOf(TextFieldValue(budget.toString())) }
            BudgetBottomSheet(
                sheetState,
                bottomSheetDismissed = { bottomSheet = false },
                amountText,
                isBudgetSet
            )
        } else {
            var amountText by remember { mutableStateOf(TextFieldValue("")) }
            BudgetBottomSheet(
                sheetState,
                bottomSheetDismissed = { bottomSheet = false },
                amountText,
                isBudgetSet
            )
        }

    }
}

@Composable
@Preview
fun SettingsScreenManagePreview() {
    Surface {
        SettingsScreenContent({}, false, 0.5f)
    }
}

@Composable
@Preview
fun SettingsScreenCreatePreview() {
    Surface {
        SettingsScreenContent({}, true, 0.2f)
    }
}


