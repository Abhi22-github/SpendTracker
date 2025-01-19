package com.example.expensetracker.Composables.utils

import com.example.expensetracker.R

enum class IconState(val number: Int, val drawableResId: Int) {
    ICON_ONE(1, R.drawable.ic_category_1),
    ICON_TWO(2, R.drawable.ic_category_2),
    ICON_THREE(3, R.drawable.ic_category_3),
    ICON_FOUR(4, R.drawable.ic_category_4),
    ICON_FIVE(5, R.drawable.ic_category_5),
    ICON_SIX(6, R.drawable.ic_category_6),
    ICON_SEVEN(7, R.drawable.ic_category_7),
    ICON_EIGHT(8, R.drawable.ic_category_8),
    ICON_NINE(9, R.drawable.ic_category_9),
    ICON_TEN(10, R.drawable.ic_category_10),
    ICON_NINTYNINE(99,R.drawable.ic_question_mark);
//    ICON_ELEVEN(11, R.drawable.ic_category_11),
//    ICON_TWELVE(12, R.drawable.ic_category_12);


    companion object {
        // This function maps a number to the corresponding Icon enum constant
        fun fromNumber(number: Int): Int? {
            return values().firstOrNull { it.number == number }?.drawableResId
        }
    }
}

enum class IconStateForType(val number: Int, val drawableResId: Int) {
    ICON_ONE(1, R.drawable.icon_expense),
    ICON_TWO(2, R.drawable.icon_income);

    companion object {
        // This function maps a number to the corresponding Icon enum constant
        fun fromNumber(number: Int): Int? {
            return values().firstOrNull { it.number == number }?.drawableResId
        }
    }
}