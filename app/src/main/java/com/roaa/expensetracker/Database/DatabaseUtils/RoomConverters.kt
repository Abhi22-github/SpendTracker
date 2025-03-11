package com.roaa.expensetracker.Database.DatabaseUtils

import androidx.room.TypeConverter
import java.math.BigDecimal

class RoomConverters {
    @TypeConverter
    fun bigDecimalToString(input: BigDecimal): String = input.toPlainString()

    @TypeConverter
    fun stringToBigDecimal(input: String): BigDecimal = BigDecimal(input)
}