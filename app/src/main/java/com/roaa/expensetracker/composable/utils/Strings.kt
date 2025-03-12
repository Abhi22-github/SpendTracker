package com.roaa.expensetracker.composable.utils

import java.util.Locale

fun String.titleCase():String = this.replaceFirstChar {
    if (it.isLowerCase()) {
        it.titlecase(Locale.getDefault())
    } else {
        it.toString()
    }
}