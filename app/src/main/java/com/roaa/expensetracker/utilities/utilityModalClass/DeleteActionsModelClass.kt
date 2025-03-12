package com.roaa.expensetracker.utilities.utilityModalClass

import com.roaa.expensetracker.utilities.DeleteAction


data class DeleteActionsModelClass(
    val header: String,
    val body: String,
    val action: DeleteAction,
)
