package com.roaa.expensetracker.Composables.Navigation

import kotlinx.serialization.Serializable

@Serializable
object ScreenA

@Serializable
data class ScreenB(
    val categoryId: Long,
    val categoryName: String,
    val categoryIconNumber: Int,
    val categoryType: String
)