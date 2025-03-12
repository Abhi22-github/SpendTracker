package com.roaa.expensetracker.converters

import com.roaa.expensetracker.database.relations.TransactionWithDetails

data class TransactionConverter(val date: String,val  transactionsList: List<TransactionWithDetails>) {
}