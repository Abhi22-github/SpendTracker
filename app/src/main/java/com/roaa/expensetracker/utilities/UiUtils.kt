package com.roaa.expensetracker.utilities

import com.roaa.expensetracker.R
import com.roaa.expensetracker.composable.color1
import com.roaa.expensetracker.composable.color2
import com.roaa.expensetracker.composable.color3
import com.roaa.expensetracker.composable.color4
import com.roaa.expensetracker.composable.color5
import com.roaa.expensetracker.composable.color6
import com.roaa.expensetracker.composable.color7
import com.roaa.expensetracker.composable.onboardingColor1
import com.roaa.expensetracker.composable.onboardingColor2
import com.roaa.expensetracker.composable.onboardingColor3
import com.roaa.expensetracker.composable.onboardingColor4
import com.roaa.expensetracker.model.uiDataModels.BarChartExpenseModel
import com.roaa.expensetracker.model.uiDataModels.OnboardingContent
import com.roaa.expensetracker.model.uiDataModels.TotalExpenseIncomeClass
import com.roaa.expensetracker.utilities.utilityModalClass.DeleteActionsModelClass
import java.math.BigDecimal
import java.time.LocalDate

fun createListForBarGraph(
    dateList: List<Pair<String, Long>>, map: Map<Long, TotalExpenseIncomeClass>
): Pair<List<BarChartExpenseModel>, BigDecimal> {
    val list = mutableListOf<BarChartExpenseModel>()
    var maxExpense = BigDecimal.ZERO
    dateList.forEach {
        var totalExpense = BigDecimal.ZERO
        var totalIncome = BigDecimal.ZERO
        if (map.containsKey(it.second)) {
            totalExpense = map[it.second]!!.totalExpense
            totalIncome = map[it.second]!!.totalIncome
            if (map[it.second]!!.totalExpense >= maxExpense) {
                maxExpense = map[it.second]!!.totalExpense
            }

        }
        list.add(BarChartExpenseModel(it.second, it.first, totalExpense, totalIncome))
    }
    return Pair(list, maxExpense)
}

val colorList = listOf(
    color1, color2, color3, color4, color5, color6, color7,
    color1, color2, color3, color4, color5, color6, color7,
)

fun convertDataToSeries(
    allDays: List<LocalDate>,
    totalValuesPerDayForMonthMap: HashMap<Long, Pair<BigDecimal, BigDecimal>>
): Pair<LinkedHashMap<String, Int>, LinkedHashMap<String, Int>> {
    val expenseListPerDayHashMap = LinkedHashMap<String, Int>()
    val incomeListPerDayHashMap = LinkedHashMap<String, Int>()
    for (day in allDays) {
        val longDay = day.toLong()
        var expense = 0
        var income = 0
        if (totalValuesPerDayForMonthMap.containsKey(longDay)) {
            expense = totalValuesPerDayForMonthMap[longDay]!!.first.toInt()
            income = totalValuesPerDayForMonthMap[longDay]!!.second.toInt()
        }
        expenseListPerDayHashMap[day.toDateWithDayName()] = expense
        incomeListPerDayHashMap[day.toDateWithDayName()] = income
    }
    return Pair(expenseListPerDayHashMap, incomeListPerDayHashMap)
}

enum class DeleteAction {
    DELETE_BANK_ACCOUNT, DELETE_AND_MIGRATE, DELETE_ALL
}

val deleteActionList = listOf(
    DeleteActionsModelClass(
        "Delete Bank Account",
        "The Bank account will be deleted, but transactions will remain.",
        DeleteAction.DELETE_BANK_ACCOUNT
    ), DeleteActionsModelClass(
        "Delete and Migrate Transactions",
        "Delete this Bank account and move transactions to another Bank Account.",
        DeleteAction.DELETE_AND_MIGRATE
    ), DeleteActionsModelClass(
        "Delete All",
        "Permanently delete this Bank account and all transactions",
        DeleteAction.DELETE_ALL
    )
)

val onboardingPageContentList = listOf(
    OnboardingContent(
        "Take Control of Your Finances!",
        "Track expenses, manage budgets, and save smarter—all in one app.",
        onboardingColor1,
        R.drawable.onboarding_1
    ),
    OnboardingContent(
        "Effortlessly Track Every Expense",
        "Log your expenses in seconds and categorize them for better insights.",
        onboardingColor2,
        R.drawable.onboarding_2
    ),
    OnboardingContent(
        "Effortlessly Track Every Expense",
        "Log your expenses in seconds and categorize them for better insights.",
        onboardingColor2,
        R.drawable.test1
    ),
    OnboardingContent(
        "Set Budgets & Stay on Track",
        "Create budgets and get alerts when you're about to exceed them",
        onboardingColor3,
        R.drawable.onboarding_3
    ),
    OnboardingContent(
        "Understand Your Spending Habits",
        "A pie chart or graph showing categorized spending.",
        onboardingColor4,
        R.drawable.onboarding_4
    )
)
