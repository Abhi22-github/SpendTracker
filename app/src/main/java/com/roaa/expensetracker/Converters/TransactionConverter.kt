package com.roaa.expensetracker.Converters

import com.roaa.expensetracker.Database.Relations.TransactionWithDetails

data class TransactionConverter(val date: String,val  transactionsList: List<TransactionWithDetails>) {
}