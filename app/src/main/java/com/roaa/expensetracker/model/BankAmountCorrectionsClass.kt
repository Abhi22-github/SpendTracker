package com.roaa.expensetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "bank_balance_corrections",
//    indices = [Index(value = ["categoryName", "categoryType"], unique = true)]
)
data class BankAmountCorrectionsClass(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var bankAccountId: Long,
    var date: Long,
    var addedTimeStamp: Long = 0,
    var finalAmount: BigDecimal,
    var difference: BigDecimal,
    var startingAmount: BigDecimal,
)