package com.roaa.expensetracker.Model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Entity(
    tableName = "category_table",
    indices = [Index(value = ["categoryName", "categoryType"], unique = true)]
)
@Serializable
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

    var isActive: Boolean

):Parcelable
