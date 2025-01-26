package com.roaa.expensetracker.Composables.utils

import androidx.compose.ui.graphics.Color
import com.roaa.expensetracker.Composables.blueColor
import com.roaa.expensetracker.Composables.greenColor
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.R

enum class IconState(val number: Int, val drawableResId: Int) {
    ICON_1(1, R.drawable.ic_category_1),
    ICON_2(2, R.drawable.ic_category_2),
    ICON_3(3, R.drawable.ic_category_3),
    ICON_4(4, R.drawable.ic_category_4),
    ICON_5(5, R.drawable.ic_category_5),
    ICON_6(6, R.drawable.ic_category_6),
    ICON_7(7, R.drawable.ic_category_7),
    ICON_8(8, R.drawable.ic_category_8),
    ICON_9(9, R.drawable.ic_category_9),
    ICON_10(10, R.drawable.ic_category_10),
    ICON_11(11, R.drawable.ic_category_11),
    ICON_12(12, R.drawable.ic_category_12),
    ICON_13(13, R.drawable.ic_category_13),
    ICON_14(14, R.drawable.ic_category_14),
    ICON_15(15, R.drawable.ic_category_15),
    ICON_16(16, R.drawable.ic_category_16),
    ICON_17(17, R.drawable.ic_category_17),
    ICON_18(18, R.drawable.ic_category_18),
    ICON_19(19, R.drawable.ic_category_19),
    ICON_20(20, R.drawable.ic_category_20),
    ICON_21(21, R.drawable.ic_category_21),
    ICON_22(22, R.drawable.ic_category_22),
    ICON_23(23, R.drawable.ic_category_23),
    ICON_24(24, R.drawable.ic_category_24),
    ICON_25(25, R.drawable.ic_category_25),
    ICON_26(26, R.drawable.ic_category_26),
    ICON_27(27, R.drawable.ic_category_27),
    ICON_28(28, R.drawable.ic_category_28),
    ICON_29(29, R.drawable.ic_category_29),
    ICON_30(30, R.drawable.ic_category_30),
    ICON_31(31, R.drawable.ic_category_31),
    ICON_32(32, R.drawable.ic_category_32),
    ICON_99(99, R.drawable.ic_question_mark);
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

enum class ColorState(val number: Int, val color: Color) {
    COLOR_1(1, blueColor),
    COLOR_2(2, greenColor),
    COLOR_3(3, orange),
    COLOR_4(4, Color.Red);

    companion object {
        // This function maps a number to the corresponding Icon enum constant
        fun fromNumber(number: Int): Color? {
            return values().firstOrNull { it.number == number }?.color
        }
    }
}