package com.roaa.expensetracker.Utilities

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.roaa.expensetracker.Composables.Navigation.Destinations
import com.roaa.expensetracker.Model.NavigationItems
import com.roaa.expensetracker.R
import java.time.LocalDate

val section1Items = listOf(
    NavigationItems(
        title = "List",
        selectedIcon = R.drawable.list_view_checked,
        unselectedIcon = R.drawable.list_view_unchecked,
        route = Destinations.ListScreen,
    ),
    NavigationItems(
        title = "Month",
        selectedIcon = R.drawable.month_view,
        unselectedIcon = R.drawable.month_view,
        route = Destinations.MonthScreen,
    ),
    NavigationItems(
        title = "Day",
        selectedIcon = R.drawable.day_view_checked,
        unselectedIcon = R.drawable.day_view_uncheckd,
        route = Destinations.DayScreen(LocalDate.now().toLong()),
    )
)
val section2Items = listOf(
    NavigationItems(
        title = "Budget",
        selectedIcon = R.drawable.budget_checked,
        unselectedIcon = R.drawable.budget_unchecked,
        route = Destinations.BudgetScreen,
    ),
    NavigationItems(
        title = "Category",
        selectedIcon = R.drawable.add_category_checked,
        unselectedIcon = R.drawable.add_category_unchecked,
        route = Destinations.CategoryScreen,
    ), NavigationItems(
        title = "Bank Accounts",
        selectedIcon = R.drawable.bank_account_checked,
        unselectedIcon = R.drawable.bank_account_unchecked,
        route = Destinations.BankAccountScreen,
    ),
    NavigationItems(
        title = "Statistics",
        selectedIcon = R.drawable.analysis_checked,
        unselectedIcon = R.drawable.analysis_unchecked,
        route = Destinations.StatisticsScreen,
    ),
    NavigationItems(
        title = "Settings",
        selectedIcon = R.drawable.settings_checked,
        unselectedIcon = R.drawable.settings_unchecked,
        route = Destinations.SettingScreen,
    )

)


class DecimalFilterTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val inputText = text.text
        val parts = inputText.split('.')
        val formattedText = when (parts.size) {
            1 -> parts[0].take(7) // Take max 7 digits before the decimal
            2 -> parts[0].take(7) + "." + parts[1].take(2) // Take max 7 before and 2 after
            else -> ""
        }
        return TransformedText(
            AnnotatedString(formattedText),
            OffsetMapping.Identity
        )
    }
}

fun getEffectivePercentageForPill(
    budgetAmountPerDay: Float,
    totalAmountForCurrentDate: Float
): Float {
    Log.d("DayComposeScreen","$budgetAmountPerDay : $totalAmountForCurrentDate")
    if (budgetAmountPerDay != 0f && totalAmountForCurrentDate != 0f) {
        return totalAmountForCurrentDate / budgetAmountPerDay
    } else
        return 0f
}