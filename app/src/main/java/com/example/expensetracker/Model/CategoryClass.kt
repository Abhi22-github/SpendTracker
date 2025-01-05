package com.example.expensetracker.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_table",
    indices = [Index(value = ["categoryName", "categoryType"], unique = true)]
)
class CategoryClass() {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @JvmField
    @ColumnInfo(name = "categoryName")
    var categoryName: String? = null

    @JvmField
    var categoryColorNumber: Int? = null

    @JvmField
    var categoryIconNumber: Int? = null

    @JvmField
    @ColumnInfo(name = "categoryType")
    var categoryType: String? = null


    constructor(
        categoryName: String?, categoryColorNumber: Int?, categoryIconNumber: Int?,
        categoryType: String?
    ) : this() {
        this.categoryName = categoryName
        this.categoryColorNumber = categoryColorNumber
        this.categoryIconNumber = categoryIconNumber
        this.categoryType = categoryType
    }
}
