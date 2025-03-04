package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.roaa.expensetracker.Composables.Navigation.NavigationManager
import com.roaa.expensetracker.Composables.Navigation.handleBackNavigation
import com.roaa.expensetracker.Composables.StatisticsComponent.AnimatedPieChart
import com.roaa.expensetracker.Composables.StatisticsComponent.PieData
import com.roaa.expensetracker.Composables.color1
import com.roaa.expensetracker.Composables.color2
import com.roaa.expensetracker.Composables.color3
import com.roaa.expensetracker.Composables.color4
import com.roaa.expensetracker.Composables.color5
import com.roaa.expensetracker.Composables.color6
import com.roaa.expensetracker.Composables.color7
import com.roaa.expensetracker.Composables.color8
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.Utilities.currentYear
import com.roaa.expensetracker.Utilities.getPreviousAndNext100Months
import com.roaa.expensetracker.Utilities.getPreviousAndNext100Weeks
import com.roaa.expensetracker.Utilities.getPreviousAndNext500Days
import com.roaa.expensetracker.Utilities.getPreviousAndNext500DaysForFilter
import java.time.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatisticsScreen(
    navHostController: NavHostController,
    navigationManager: NavigationManager,
) {
    val options = listOf("Day", "Week", "Month")
    var selectedIndex by remember { mutableIntStateOf(0) }
    val rememberLazyListState = rememberLazyListState()
    var filterRowContent by remember { mutableStateOf(getPreviousAndNext500Days(LocalDate.now())) }
    var selectedIndexForFilterChip by remember { mutableStateOf(filterRowContent.size / 2) }
    LaunchedEffect(selectedIndex) {
        filterRowContent = when (selectedIndex) {
            0 -> getPreviousAndNext500DaysForFilter(LocalDate.now())
            1 -> getPreviousAndNext100Weeks()
            2 -> getPreviousAndNext100Months(LocalDate.now())
            else -> getPreviousAndNext500Days(LocalDate.now())
        }
        selectedIndexForFilterChip = filterRowContent.size / 2
        rememberLazyListState.scrollToItem((filterRowContent.size / 2))
    }

    BackHandler() {
        handleBackNavigation(navigationManager)
    }
    Scaffold(
        topBar = {
            TopBar(title = "Statistics",
                showDelete = false,
                sendUserBackToPreviousActivity = { handleBackNavigation(navigationManager) },
                delete = {})
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            Row {
                Column {
                    ThreeOptionTextSwitch(
                        selectedIndex = selectedIndex,
                        items = options,
                        onSelectionChange = {
                            selectedIndex = it
                        }
                    )
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Rounded.Tune, contentDescription = "Settings")
                }
            }
            Spacer(Modifier.height(8.dp))
            LazyRow(
                state = rememberLazyListState,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(filterRowContent) { index, text ->
                    ChipsForFilter(
                        index,
                        selectedIndexForFilterChip,
                        text
                    ) { selectedIndexForFilterChip = it }
                }

            }
            Spacer(Modifier.height(32.dp))
            Box(contentAlignment = Alignment.Center) {
                AnimatedPieChart(
                    Modifier.size(240.dp), listOf(
                        PieData("Food", 40, color1),
                        PieData("Transportation", 62, color2),
                        PieData("Fuel", 50, color3),
                        PieData("Other", 100, color4),
                        PieData("Food", 40, color5),
                        PieData("Transportation", 62, color6),
                        PieData("Fuel", 50, color7),
                        PieData("Other", 100, color8)
                    )
                )
                Text("Testing")
            }
            //Test(Modifier)
        }
    }
}

@Composable
fun ChipsForFilter(
    index: Int,
    selectedIndexForFilterChip: Int,
    text: String,
    selectChip: (Int) -> Unit
) {
    val temp = if (text.split(",").get(1) == currentYear) text.split(",")[0] else text
    FilterChip(
        onClick = { selectChip(index) },
        label = {
            Text(text = temp)
        },
        selected = index == selectedIndexForFilterChip,
        leadingIcon = if (index == selectedIndexForFilterChip) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.1.dp, MaterialTheme.colorScheme.outline),
        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    )
}