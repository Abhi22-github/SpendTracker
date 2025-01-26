package com.roaa.expensetracker.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_accounts",)
data class BankAccountsClass(
    @PrimaryKey
    val id: Long,
    val initialAmount: Long,
    val currentAmount: Long,
    val bankName: String,
    val accountType: String,
)