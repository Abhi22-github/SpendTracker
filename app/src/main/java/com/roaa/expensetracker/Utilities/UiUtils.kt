package com.roaa.expensetracker.Utilities

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import com.roaa.expensetracker.Model.UiDateModels.BarChartExpenseModel

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

fun calculateBarPercentageHeight(maxHeight: Dp, maxExpense: Float, expenseAmount: Float): Dp {
    if (maxExpense == 0f)
        return 0.dp

    if (expenseAmount == 0f)
        return 0.dp

    return (maxHeight.value * expenseAmount / maxExpense).dp
}