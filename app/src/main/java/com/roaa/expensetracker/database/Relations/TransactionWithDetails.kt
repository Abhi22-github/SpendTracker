package com.roaa.expensetracker.database.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.Model.TransactionClass
import kotlinx.serialization.Serializable

@Serializable
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