package com.roaa.expensetracker.Converters

import com.roaa.expensetracker.Model.TransactionClass

data class TransactionConverter(val date: String,val  transactionsList: List<TransactionClass>) {
}