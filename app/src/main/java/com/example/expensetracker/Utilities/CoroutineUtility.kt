package com.example.expensetracker.Utilities

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun launchCoroutine(currentItem: Unit) {
    GlobalScope.launch { currentItem }
}