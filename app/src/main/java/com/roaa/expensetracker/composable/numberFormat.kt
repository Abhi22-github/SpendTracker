package com.roaa.expensetracker.composable

import android.content.Context
import com.roaa.expensetracker.R
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

// Get localised float divider (',' or '.')
fun getFloatDivider(): String {
    val numberFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.getDefault())

    numberFormat.maximumFractionDigits = 1
    numberFormat.minimumFractionDigits = 1

    val formattedValue = numberFormat.format(1.0)

    return formattedValue.substring(1, 2)
}

fun numberFormat(
    context: Context,
    value: BigDecimal,
    trimDecimalPlaces: Boolean = false,
    forceShowAfterDot: Boolean = false,
    maximumFractionDigits: Int = if (forceShowAfterDot) 5 else 2,
    minimumFractionDigits: Int = if (forceShowAfterDot) 1 else 0,
): String {
    var valueFinal = value
    var decimalPlace = ""
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())


    formatter.maximumFractionDigits = maximumFractionDigits
    formatter.minimumFractionDigits = minimumFractionDigits


    val thousand = BigDecimal(1000)
    var overflow = false
    if (trimDecimalPlaces) {
        valueFinal = if (value >= thousand.pow(4) * BigDecimal(100)) {
            decimalPlace = context.getString(R.string.trillion_numerical_shorthand)
            overflow = true
            100.toBigDecimal()
        } else if (value >= thousand.pow(4)) {
            decimalPlace = context.getString(R.string.trillion_numerical_shorthand)
            value / (thousand.pow(4))
        } else if (value >= thousand.pow(3)) {
            decimalPlace = context.getString(R.string.billion_numerical_shorthand)
            value / (thousand.pow(3))
        } else if (value >= thousand.pow(2)) {
            decimalPlace = context.getString(R.string.million_numerical_shorthand)
            value / (thousand.pow(2))
        } else if (value >= thousand * BigDecimal(100)) {
            decimalPlace = context.getString(R.string.thousand_numerical_shorthand)
            value / thousand
        } else {
            value
        }
    }


    var formattedValue = formatter.format(valueFinal)

    return "${if(overflow) "> " else ""}$formattedValue $decimalPlace"
}