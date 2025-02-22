package com.roaa.expensetracker.Utilities

import com.roaa.expensetracker.Model.TotalExpenseIncomeClass
import com.roaa.expensetracker.Model.UiDateModels.BarChartExpenseModel

fun createListForBarGraph(
    dateList: List<Pair<String, Long>>,
    map: Map<Long, TotalExpenseIncomeClass>
): List<BarChartExpenseModel> {
    val list = mutableListOf<BarChartExpenseModel>()
    dateList.forEach {
        var totalExpense = 0f
        var totalIncome = 0f
        if (map.containsKey(it.second)) {
            totalExpense = map[it.second]!!.totalExpense
            totalIncome = map[it.second]!!.totalIncome
        }

        list.add(BarChartExpenseModel(it.second, it.first, totalExpense, totalIncome))
    }
    return list
}