package com.roaa.expensetracker.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_accounts")
data class BankAccountsClass(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val initialAmount: Long,
    val currentAmount: Long,
    val bankName: String,
    val cardColorNumber: Int,
    val cardIconNumber: Int,
    val accountType: String,
)