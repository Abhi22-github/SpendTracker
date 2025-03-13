package com.roaa.expensetracker.database.databaseUtils

import androidx.room.TypeConverter
import java.math.BigDecimal

class RoomConverters {
    @TypeConverter
    fun bigDecimalToString(input: BigDecimal?): String = input?.toString() ?: "0"

    @TypeConverter
    fun stringToBigDecimal(input: String?): BigDecimal {
        return if (input.isNullOrEmpty()) BigDecimal.ZERO
        else BigDecimal(input)
    }
}