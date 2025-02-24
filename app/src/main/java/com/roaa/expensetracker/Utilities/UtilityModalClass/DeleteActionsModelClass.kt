package com.roaa.expensetracker.Utilities.UtilityModalClass

import com.roaa.expensetracker.Utilities.DeleteAction


data class DeleteActionsModelClass(
    val header: String,
    val body: String,
    val action: DeleteAction,
)
