package com.roaa.expensetracker.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.roaa.expensetracker.Actions
import com.roaa.expensetracker.Adapters.CategoryViewAdapter
import com.roaa.expensetracker.AddCategoryBottomSheet
import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.R
import com.roaa.expensetracker.Utilities.Constants
import com.roaa.expensetracker.ViewModels.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class IncomeCategoryActivity : AppCompatActivity(), Actions {
    private var mContext: Context? = null

    private val imageButtonBackButton: ImageButton? = null
    private var fabAddCategory: FloatingActionButton? = null
    private var recyclerViewCategoryExpense: RecyclerView? = null
    private var categoryViewAdapter: CategoryViewAdapter? = null

    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var toolbarExpenseCategoryToolbar: MaterialToolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)


        //initializing the vars
        initVars()

        //initializing the views
        initView()

        //send user back to the previous activity on back icon pressed
        toolbarExpenseCategoryToolbar.setNavigationOnClickListener { v: View? -> onBackPressedDispatcher.onBackPressed() }

        //open the bottom sheet when user clicks on fab add button
        fabAddCategory!!.setOnClickListener { v: View? -> openBottomSheetModel() }

        //set up the expense recycler view on observer live data
        setUpCategoryExpenseRecyclerView()
    }

    private fun initVars() {
        mContext = this
    }

    private fun initView() {
        //  imageButtonBackButton = findViewById(R.id.imageButton_back_categoryActivity);
        fabAddCategory = findViewById(R.id.fab_addCategory_categoryActivity)
        recyclerViewCategoryExpense =
            findViewById(R.id.recyclerView_categoryExpense_categoryActivity)
        toolbarExpenseCategoryToolbar =
            findViewById(R.id.toolbar_expenseCategoryToolbar_expenseCategoryActivity)
        toolbarExpenseCategoryToolbar.setTitle("Income Category")
    }

    private fun openBottomSheetModel() {
        val categoryClass = CategoryClass(
            id = 0,
            categoryName = "",
            categoryColorNumber = 1,
            categoryIconNumber = 1,
            categoryType = Constants.INCOME
        )
        val addCategoryBottomSheet = AddCategoryBottomSheet(categoryClass)
        addCategoryBottomSheet.show(supportFragmentManager, "addCategory")
    }

    private fun setUpCategoryExpenseRecyclerView() {
        recyclerViewCategoryExpense!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        categoryViewModel.onlyIncomeCategoryNames.onEach {
            categoryViewAdapter = CategoryViewAdapter(mContext, it)
            recyclerViewCategoryExpense!!.adapter = categoryViewAdapter
        }

    }

    override fun onDeleteCategory(categoryClass: CategoryClass) {
        categoryViewModel.deleteCategoryFromDatabase(categoryClass)
    }
}