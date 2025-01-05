package com.example.expensetracker

import com.example.expensetracker.Model.CategoryClass

interface Actions {
   open fun onDeleteCategory(categoryClass: CategoryClass)
}