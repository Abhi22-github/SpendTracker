package com.example.expensetracker.ViewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import javax.inject.Inject

data class SystemBarState(
    val statusBarColor: Color,
    val statusBarDarkIcons: Boolean,
    val navigationBarDarkIcons: Boolean,
    val navigationBarColor: Color,
)

class AppViewModel @Inject constructor() : ViewModel() {
    var statusBarStack: MutableList<() -> SystemBarState> =
        emptyList<() -> SystemBarState>().toMutableList()
}