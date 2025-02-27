package com.roaa.expensetracker.Composables.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.Navigation.handleBackNavigation
import com.roaa.expensetracker.Composables.ThemeMode
import com.roaa.expensetracker.Composables.components.SingleItemRadioButton
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
    rootNavController: NavHostController,
    navigationManager: NavigationManager,
    sendUserBack: () -> Unit,
    preferenceViewModel: PreferencesViewModel = hiltViewModel()
) {

    BackHandler() {
        handleBackNavigation(navigationManager)
    }
    Scaffold(
        topBar = {
            TopBar(title = "Settings",
                showDelete = false,
                sendUserBackToPreviousActivity = { handleBackNavigation(navigationManager) },
                delete = {})
        },
    ) {
        SettingsScreenContent(Modifier.padding(it))
    }
}

val startEndPadding = 16.dp
val topBottomPadding = 0.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    preferenceViewModel: PreferencesViewModel = hiltViewModel()
) {
    val themeSelected by preferenceViewModel.getThemeMode.collectAsState(ThemeMode.SYSTEM.toString())
    val showExperimentalComponent by preferenceViewModel.showExperimentalComponent.collectAsState(false)
    val sheetState = rememberModalBottomSheetState()
    var bottomSheet by remember { mutableStateOf(false) }
    val list = listOf("LIGHT", "NIGHT", "SYSTEM")
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    Column(modifier) {
//        TopBar(title = "Settings",false,{sendUserBack()},{})
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
                        text = "Experiment Components", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(startEndPadding, topBottomPadding)
                    )
                    Text(
                        text = "Show all experimental components",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
                        modifier = Modifier.padding(startEndPadding, topBottomPadding)
                    )
                }
                Switch(
                    checked = showExperimentalComponent,
                    onCheckedChange = {
                        scope.launch {
                            preferenceViewModel.setExperimentalComponentsState(it)
                        }
                    },
                    modifier = Modifier
                        .weight(.3f)
                        .align(Alignment.CenterVertically)
                )
            }

            Spacer(Modifier.height(32.dp))

        }
    }
}

@Composable
@Preview
fun SettingsScreenManagePreview() {
    Surface {
        SettingsScreenContent()
    }
}

@Composable
@Preview
fun SettingsScreenCreatePreview() {
    Surface {
        SettingsScreenContent()
    }
}


