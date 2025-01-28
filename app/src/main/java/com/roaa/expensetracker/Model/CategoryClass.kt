package com.roaa.expensetracker.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_table",
    indices = [Index(value = ["categoryName", "categoryType"], unique = true)]
)
data class CategoryClass(
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var categoryId: Long,

    @JvmField
    @ColumnInfo(name = "categoryName")
    var categoryName: String,

    @JvmField
    var categoryColorNumber: Int,

    @JvmField
    var categoryIconNumber: Int,

    @JvmField
    @ColumnInfo(name = "categoryType")
    var categoryType: String,


    )
