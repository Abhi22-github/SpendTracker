package com.roaa.expensetracker

import com.roaa.expensetracker.Model.CategoryClass

interface Actions {
   open fun onDeleteCategory(categoryClass: CategoryClass)
}