package com.roaa.expensetracker.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.Utilities.Constants.EXPENSE
import com.roaa.expensetracker.Utilities.Constants.INCOME
import com.roaa.expensetracker.Utilities.UiState
import com.roaa.expensetracker.database.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {
    //flow for Ui states
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    //flow for only Income Category names
    private val _onlyIncomeCategoryNames = MutableStateFlow<List<CategoryClass>>(listOf())
    val onlyIncomeCategoryNames: StateFlow<List<CategoryClass>> = _onlyIncomeCategoryNames

    //flow for only Expense Category names
    private val _onlyExpenseCategoryNames = MutableStateFlow<List<CategoryClass>>(listOf())
    val onlyExpenseCategoryNames: StateFlow<List<CategoryClass>> = _onlyExpenseCategoryNames

    //flow for only Expense Category names
    private var _categoryList = MutableStateFlow<List<CategoryClass>>(listOf())
    val categoryList: StateFlow<List<CategoryClass>> = _categoryList

    private var _allCategoryList = MutableStateFlow<List<CategoryClass>>(listOf())
    val allCategoryList: StateFlow<List<CategoryClass>> = _allCategoryList

//    val allCategoryNames: Flow<List<CategoryClass>>
//        get() = categoryRepository.allCategories


    init {
        getAllCategoriesFromDatabase()
        getOnlyExpenseCategoryNames()
        getOnlyIncomeCategoryNames()
    }

    fun getAllCategoriesFromDatabase() {
        viewModelScope.launch {
            loading()
            categoryRepository.allCategories.catch {
                error(it)
            }.collect {
                _allCategoryList.value = it
                completed()
            }
        }
    }


    fun getOnlyExpenseCategoryNames() {
        viewModelScope.launch {
            loading()
            categoryRepository.getOnlyExpenseCategories().catch { error(it) }
                .collect { categoryClassesList ->
                    _onlyExpenseCategoryNames.value = categoryClassesList
                    completed()
                }
        }
    }

    fun getCorrespondingList(type: String) {
        viewModelScope.launch {
            if (type == EXPENSE) {
                _onlyExpenseCategoryNames.collect { categoryList ->
                    _categoryList.value = categoryList
                }
            } else if (type == INCOME) {
                _onlyIncomeCategoryNames.collect { categoryList ->
                    _categoryList.value = categoryList
                }
            }
        }
    }


    fun getOnlyIncomeCategoryNames() {
        viewModelScope.launch {
            loading()
            categoryRepository.getOnlyIncomeCategories().catch { error(it) }
                .collect { categoryClassesList ->
                    _onlyIncomeCategoryNames.value = categoryClassesList
                    completed()
                }
        }
    }




    //to delete categories from database
    fun deleteCategoryFromDatabase(categoryClass: CategoryClass) {
        viewModelScope.launch {
            categoryRepository.delete(categoryClass)
        }
    }

    private fun storeCategoryInDatabase(categoryClass: CategoryClass) {
        if (categoryClass.categoryId == 0L) {
            //new category insert
            viewModelScope.launch {
                categoryRepository.insert(categoryClass)
            }
        } else {
            //existing category update
            viewModelScope.launch {
                categoryRepository.update(categoryClass)
            }
        }
    }

    fun loading() {
        _uiState.value = UiState.Loading
    }

    fun completed() {
        _uiState.value = UiState.Success
    }

    fun error(error: Throwable) {
        _uiState.value = UiState.Error(error.toString())
        Log.d("Hello Error reason", error.toString())
    }

    fun validateCategoryData(
        categoryId: Long,
        categoryName: String,
        categoryIcon: Int,
        categoryType: String
    ) {
        val categoryClass = CategoryClass(
            categoryId = categoryId,
            categoryName = categoryName,
            categoryColorNumber = 1,
            categoryIconNumber = categoryIcon,
            categoryType = categoryType,
            isActive = true
        )
        storeCategoryInDatabase(categoryClass)
    }
}