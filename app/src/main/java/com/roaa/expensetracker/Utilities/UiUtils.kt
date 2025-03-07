package com.roaa.expensetracker.Utilities

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import com.roaa.expensetracker.Model.UiDateModels.BarChartExpenseModel
import com.roaa.expensetracker.Utilities.UtilityModalClass.DeleteActionsModelClass
import java.time.LocalDate

fun createListForBarGraph(
    dateList: List<Pair<String, Long>>,
    map: Map<Long, TotalExpenseIncomeClass>
): Pair<List<BarChartExpenseModel>, Float> {
    val list = mutableListOf<BarChartExpenseModel>()
    var maxExpense = 0f
    dateList.forEach {
        var totalExpense = 0f
        var totalIncome = 0f
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

fun convertDataToSeries(
    allDays: List<LocalDate>,
    totalValuesPerDayForMonthMap: HashMap<Long, Pair<Float, Float>>
): Pair<LinkedHashMap<String, Int>, LinkedHashMap<String, Int>> {
    val expenseListPerDayHashMap = LinkedHashMap<String,Int>()
    val incomeListPerDayHashMap = LinkedHashMap<String,Int>()
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
    return Pair(expenseListPerDayHashMap,incomeListPerDayHashMap)
}

fun calculateBarPercentageHeight(maxHeight: Dp, maxExpense: Float, expenseAmount: Float): Dp {
    if (maxExpense == 0f)
        return 0.dp
    if (expenseAmount == 0f)
        return 0.dp

    return (maxHeight.value * expenseAmount / maxExpense).dp
}

enum class DeleteAction {
    DELETE_BANK_ACCOUNT,
    DELETE_AND_MIGRATE,
    DELETE_ALL
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
