package com.roaa.expensetracker.composable.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    showDelete: Boolean,
    sendUserBackToPreviousActivity: () -> Unit,
    delete: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = typography.titleLarge,
                modifier = Modifier
            )
        },
        navigationIcon = {
            IconButton(onClick = { sendUserBackToPreviousActivity() }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actions = {
            if (showDelete) {
                IconButton(onClick = { delete() }) {
                    Icon(Icons.Filled.Delete, "backIcon")
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetTopBar(
    title: String,
    showSetting: Boolean,
    sendUserBackToPreviousActivity: () -> Unit,
    settingsClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = typography.titleLarge,
                modifier = Modifier
            )
        },
        navigationIcon = {
            IconButton(onClick = { sendUserBackToPreviousActivity() }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actions = {
            if (showSetting) {
                IconButton(onClick = { settingsClicked() }) {
                    Icon(Icons.Rounded.Settings, "settings_icon")
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarForTransactionDetailsScreen(
    title: String,
    showDelete: Boolean,
    sendUserBackToPreviousActivity: () -> Unit,
    delete: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = typography.titleLarge,
                modifier = Modifier
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(start = 20.dp),
                onClick = { sendUserBackToPreviousActivity() },
                colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.surface)
            ) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actions = {
            if (showDelete) {
                IconButton(onClick = { delete() }) {
                    Icon(Icons.Filled.Delete, "backIcon")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
    )
}


@Composable
@Preview
fun TopBarPreview() {
    TopBar("Hello", true, {}, {})
}