package com.roaa.expensetracker.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.roaa.expensetracker.model.BankAccountsClass
import com.roaa.expensetracker.model.CategoryClass
import com.roaa.expensetracker.model.TransactionClass

data class TransactionWithDetails(
    @Embedded val transaction: TransactionClass,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: CategoryClass,

    @Relation(
        parentColumn = "bankAccountId",
        entityColumn = "bankAccountId"
    )
    val BankAccount: BankAccountsClass
)