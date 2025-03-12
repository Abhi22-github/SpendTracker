package com.roaa.expensetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "transaction_table")
data class TransactionClass(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var type: String,
    var amount: BigDecimal,
    var note: String,
    var dateWithTime: Long,
    var date: Long,
    @ColumnInfo(defaultValue = "1")
    var includeInRespectiveBudget:Boolean,
    var categoryId: Long = 0, // foreign key for category
    var bankAccountId: Long = 0,// foreign key with bank
)
