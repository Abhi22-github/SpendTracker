package com.example.expensetracker;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.Adapters.CategoryExpenseViewAdapter;
import com.example.expensetracker.Model.CategoryClass;
import com.example.expensetracker.Utilities.Constants;
import com.example.expensetracker.ViewModels.AddActivityViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ExpenseCategoryActivity extends AppCompatActivity {
    private Context mContext;

    private ImageButton imageButtonBackButton;
    private FloatingActionButton fabAddCategory;
    private RecyclerView recyclerViewCategoryExpense;
    private CategoryExpenseViewAdapter categoryExpenseViewAdapter;
    private AddActivityViewModel viewmodel;
    private MaterialToolbar toolbarExpenseCategoryToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        //initializing the viewmodel and passing the application
        viewmodel = new ViewModelProvider(this).get(AddActivityViewModel.class);
        viewmodel.initializeDatabaseRepository(getApplication());

        //initializing the vars
        initVars();

        //initializing the views
        initView();

        //send user back to the previous activity on back icon pressed
        toolbarExpenseCategoryToolbar.setNavigationOnClickListener(v ->getOnBackPressedDispatcher().onBackPressed() );

        //open the bottom sheet when user clicks on fab add button
        fabAddCategory.setOnClickListener(v -> openBottomSheetModel());

        //set up the expense recycler view on observer live data
        setUpCategoryExpenseRecyclerView();

    }

    private void initVars(){
        mContext = this;
    }

    private void initView() {
      //  imageButtonBackButton = findViewById(R.id.imageButton_back_categoryActivity);
        fabAddCategory = findViewById(R.id.fab_addCategory_categoryActivity);
        recyclerViewCategoryExpense = findViewById(R.id.recyclerView_categoryExpense_categoryActivity);
        toolbarExpenseCategoryToolbar = findViewById(R.id.toolbar_expenseCategoryToolbar_expenseCategoryActivity);
        toolbarExpenseCategoryToolbar.setTitle("Expense Category");
    }

    private void openBottomSheetModel(){
        AddCategoryBottomSheet addCategoryBottomSheet = new AddCategoryBottomSheet(Constants.expense);
        addCategoryBottomSheet.show(getSupportFragmentManager(),"addCategory");
    }

    private void setUpCategoryExpenseRecyclerView(){
        recyclerViewCategoryExpense.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        viewmodel.getOnlyExpenseCategoryNames().observe(this, new Observer<List<CategoryClass>>() {
            @Override
            public void onChanged(List<CategoryClass> categoryClassList) {
                categoryExpenseViewAdapter = new CategoryExpenseViewAdapter(mContext,categoryClassList);
                recyclerViewCategoryExpense.setAdapter(categoryExpenseViewAdapter);
            }
        });

    }

}