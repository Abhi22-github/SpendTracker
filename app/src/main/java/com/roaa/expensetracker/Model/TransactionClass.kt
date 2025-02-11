package com.roaa.expensetracker.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "transaction_table")
data class TransactionClass(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var type: String ,
    var amount: Float,
    var note: String ,
    var dateWithTime: Long ,
    var date: Long,
    var categoryId: Long = 0, // foreign key for category
    var bankAccountId: Long = 0 ,// foreign key with bank
):Parcelable
