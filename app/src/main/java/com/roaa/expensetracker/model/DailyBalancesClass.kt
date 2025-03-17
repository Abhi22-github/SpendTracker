package com.roaa.expensetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "daily_balances",
//    indices = [Index(value = ["categoryName", "categoryType"], unique = true)]
)
data class DailyBalancesClass(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var bankAccountId: Long,
    var date: Long,
    var remBankBalance: BigDecimal,
    var dayTotalExpense: BigDecimal,
    var dayTotalIncome: BigDecimal,
    var isSetByUser: Boolean,
)