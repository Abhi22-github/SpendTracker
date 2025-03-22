package com.roaa.expensetracker.utilities

import android.content.Context
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.roaa.expensetracker.R
import com.roaa.expensetracker.composable.navigation.Destinations
import com.roaa.expensetracker.hilt.AllViewModel
import com.roaa.expensetracker.model.uiDataModels.CurrencyClass
import com.roaa.expensetracker.model.uiDataModels.NavigationItems
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.Currency
import java.util.Locale

val decimalFormat = DecimalFormat("#,##,##0.00")

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

fun appStartingChecks(context: Context, viewModel: AllViewModel) {
    checkIfBudgetIsExpired(context, viewModel)
}

@OptIn(DelicateCoroutinesApi::class)
fun checkIfBudgetIsExpired(context: Context, viewModel: AllViewModel) {
//    GlobalScope.launch {
//        viewModel.budgetViewModel.getCurrentBudget().catch { }.collect { budgetModelClass ->
//            if (budgetModelClass.budgetEndDate < LocalDate.now().toLong()) {
//                val temp = budgetModelClass.copy(isActive = false)
//                viewModel.budgetViewModel.updateBudget(temp)
//                sendNotification(
//                    context,
//                    generalNotificationChannel,
//                    "Budget Expired",
//                    "Your budget has expired"
//                )
//            }
//
//        }
//    }
}


fun getCountryCurrencyList(): List<CurrencyClass> {
    val countryList = mutableListOf<CurrencyClass>()

    Locale.getISOCountries().forEach { countryCode ->
        val locale = Locale("", countryCode)
        val countryName = locale.displayCountry
        val currency = try {
            Currency.getInstance(locale)
        } catch (e: Exception) {
            null
        }
        val currencyCode = currency?.currencyCode ?: "N/A"
        val currencySymbol = currency?.symbol ?: "N/A"
        val currencyDisplayName = currency?.displayName ?: "N/A"
        val flag = getCountryFlagEmoji(countryCode)

        countryList.add(
            CurrencyClass(
                countryName,
                currencyCode,
                currencyDisplayName,
                currencySymbol,
                flag
            )
        )

    }
    return countryList.sortedBy { it.countryName }
}

fun getCountryFlagEmoji(countryCode: String): String {
    return countryCode.uppercase().map { char ->
        Character.toChars(127397 + char.code).joinToString("")
    }.joinToString("")
}

