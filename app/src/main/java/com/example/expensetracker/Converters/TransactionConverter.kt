package com.example.expensetracker.Converters

import com.example.expensetracker.Model.TransactionClass

data class TransactionConverter(val date: String,val  transactionsList: List<TransactionClass>) {
}