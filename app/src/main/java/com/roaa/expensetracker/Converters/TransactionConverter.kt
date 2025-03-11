package com.roaa.expensetracker.Converters

import com.roaa.expensetracker.database.Relations.TransactionWithDetails

data class TransactionConverter(val date: String,val  transactionsList: List<TransactionWithDetails>) {
}