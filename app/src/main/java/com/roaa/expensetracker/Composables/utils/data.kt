package com.roaa.expensetracker.Composables.utils

val iconsList = (1..32).toList()

val colorList = (1..8).toList()

enum class DistributionMethod(val number: Int, val type: String) {
    SPILLOVER(1, "SpillOver"),
    DEFAULT(2, "Default");

    companion object {
        // This function maps a number to the corresponding Icon enum constant
        fun fromNumber(number: Int): String {
            return DistributionMethod.values().firstOrNull { it.number == number }?.type
                ?: "SpillOver"
        }

        fun fromType(type: String): Int {
            return DistributionMethod.values().firstOrNull { it.type == type }?.number ?: 1
        }
    }
}

val distributionChoiceList = listOf(
    Triple("Default", "We will not distribute the day's remaining balance to next day",DistributionMethod.DEFAULT),
    Triple("Distribute", "The remaining amount will be distributed on the remaining days",DistributionMethod.SPILLOVER)
)