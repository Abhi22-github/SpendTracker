package com.roaa.expensetracker.composable.utils

val iconsList = (1..32).toList()

val colorList = (1..8).toList()

enum class DistributionMethod(val number: Int, val type: String) {
    DEFAULT(1, "Default"),
    DISTRIBUTION(2, "Even Distribution"),
    SPILLOVER(3, "Spillover");

    companion object {
        // This function maps a number to the corresponding Icon enum constant
        fun fromNumber(number: Int): String {
            return DistributionMethod.values().firstOrNull { it.number == number }?.type
                ?: "Default"
        }

        fun fromNumberToObject(number: Int): DistributionMethod {
            return DistributionMethod.values().firstOrNull { it.number == number }
                ?: DistributionMethod.DEFAULT
        }

        fun fromType(type: String): Int {
            return DistributionMethod.values().firstOrNull { it.type == type }?.number ?: 1
        }
    }
}

enum class ActionTypes() {
    ADD,
    EDIT,
}

val distributionChoiceList = listOf(
//    Triple(
//        "Default",
//        "We will not distribute the day's remaining balance to next day",
//        DistributionMethod.DEFAULT
//    ),
//    Triple(
//        "Distribute",
//        "The remaining amount will be distributed on the remaining days",
//        DistributionMethod.SPILLOVER
//    )
    Triple(
        "Default",
        "Remaining balance will not be carried forward or distributed to future days.",
        DistributionMethod.DEFAULT
    ),
    Triple(
        "Even Distribution",
        "Remaining balance will be evenly distributed across the remaining days of the budget period.",
        DistributionMethod.DISTRIBUTION
    ),
    Triple(
        "Distribution to Only Next Day",
        "Remaining balance will be distributed to next day",
        DistributionMethod.SPILLOVER
    ),
)