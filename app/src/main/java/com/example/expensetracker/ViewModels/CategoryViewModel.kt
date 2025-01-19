package com.example.expensetracker.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.expensetracker.Database.CategoryRepository
import com.example.expensetracker.Events.EventMessage
import com.example.expensetracker.Model.CategoryClass
import dagger.hilt.android.lifecycle.HiltViewModel
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) : ViewModel() {

    val categoryNames: LiveData<List<CategoryClass>>
        get() = categoryRepository.allCategories

    val onlyIncomeCategoryNames: LiveData<List<CategoryClass>>
        get() = categoryRepository.onlyIncomeCategories

    val onlyExpenseCategoryNames: LiveData<List<CategoryClass>>
        get() = categoryRepository.onlyExpenseCategories

    fun validateCategoryData(categoryClass: CategoryClass) {
        if (categoryClass.categoryName!!.isEmpty()) {
            // textInputLayoutName.setError("Name field can't be empty");
            EventBus.getDefault().post(EventMessage(1, "Name field can't be empty"))
        } else if (categoryClass.categoryName!!.length < 3) {
            //textInputLayoutName.setError("Name must have at least 3 letters");
            EventBus.getDefault().post(EventMessage(12, "Name must have at least 3 letters"))
        } else if (categoryClass.categoryType!!.isEmpty()) {
            EventBus.getDefault().post(EventMessage(2, "Please select a category type"))
        } else {
            storeCategoryInDatabase(categoryClass)
        }
    }


    fun fillCategoriesInDatabase(categoryClassesList: ArrayList<CategoryClass>) {
        for (categoryClass in categoryClassesList) {
            categoryRepository.insert(categoryClass)
        }
        EventBus.getDefault().post(EventMessage(9, "success"))
    }

    //to delete categories from database
    fun deleteCategoryFromDatabase(categoryClass: CategoryClass?) {
        categoryRepository.delete(categoryClass)
    }

    private fun storeCategoryInDatabase(categoryClass: CategoryClass) {
        if (categoryClass.id == 0L) {
            //new category insert
            categoryRepository.insert(categoryClass)
        } else {
            //existing category update
            categoryRepository.update(categoryClass)
        }

        EventBus.getDefault().post(EventMessage(3, "closing bottom sheet"))
    }
}