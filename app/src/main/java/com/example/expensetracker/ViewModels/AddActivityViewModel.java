package com.example.expensetracker.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensetracker.Database.CategoryRepository;
import com.example.expensetracker.Events.EventMessage;
import com.example.expensetracker.Model.CategoryClass;
import com.example.expensetracker.Model.TransactionClass;
import com.example.expensetracker.Database.TransactionRepository;
import com.example.expensetracker.Utilities.Constants;
import com.google.android.material.chip.Chip;

import org.greenrobot.eventbus.EventBus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddActivityViewModel extends ViewModel {
    private TransactionRepository transactionRepository;
    private CategoryRepository categoryRepository;
    private String[] chipsName;
    private List<Chip> chipsList;
    private String currentSelectedExpenseCategory = "";
    private String currentSelectedIncomeCategory = "";
    private Integer amount = 0;
    private String note = "";
    private Long date;
    private DateTimeFormatter dateFormatter;
    private String transactionType;
    private String category;
    private SharedPreferences sharedPreferences;


//    public  MutableLiveData<String> liveData = new MutableLiveData<>();


    AddActivityViewModel() {
        chipsName = new String[]{};
        chipsList = new ArrayList<>();
        date = Calendar.getInstance().getTimeInMillis();
        transactionType = Constants.EXPENSE;
    }

    public void initializeDatabaseRepository(Application application) {
        transactionRepository = new TransactionRepository(application);
        categoryRepository = new CategoryRepository(application);
    }


    public LiveData<List<CategoryClass>> getCategoryNames() {
        return categoryRepository.getAllCategories();
    }

    public LiveData<List<CategoryClass>> getOnlyIncomeCategoryNames() {
        return categoryRepository.getOnlyIncomeCategories();
    }

    public LiveData<List<CategoryClass>> getOnlyExpenseCategoryNames() {
        return categoryRepository.getOnlyExpenseCategories();
    }

    public void storeCurrentExpenseSelectChip(String s) {
        currentSelectedExpenseCategory = s;
        Log.d("Chips Expense ", currentSelectedExpenseCategory);
    }

    public void storeCurrentIncomeSelectChip(String s) {
        currentSelectedIncomeCategory = s;
        Log.d("Chips Income ", currentSelectedIncomeCategory);
    }

    public void setTransactionTypeInViewModel(String transaction) {
        transactionType = transaction;
    }

    public String getTransactionTypeFromViewModel() {
        return transactionType;
    }


    public void validateFormData(String amount, String note, Context mContext) {
        //to get,separate and validate data from both expense and income
        if (amount.isEmpty()) {
            EventBus.getDefault().post(new EventMessage(4, "Amount can't be zero"));
        } else if (transactionType.equals(Constants.EXPENSE)) {
            category = currentSelectedExpenseCategory;
            if (currentSelectedExpenseCategory.isEmpty()) {
                EventBus.getDefault().post(new EventMessage(5, "Please select a category"));
            } else {
                //store the data
                Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                storeFormDataInDatabase(transactionType, amount, note, category, date);
            }
        } else if (transactionType.equals(Constants.INCOME)) {
            category = currentSelectedIncomeCategory;
            if (currentSelectedIncomeCategory.isEmpty()) {
                EventBus.getDefault().post(new EventMessage(5, "Please select a category"));
            } else {
                //store the data
                Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                storeFormDataInDatabase(transactionType, amount, note, category, date);
            }
        }

    }

    private void storeFormDataInDatabase(String expense, String amount, String note, String category, Long dateWithTime) {
        // on below line we are creating
        // a variable for our modal class.
        TransactionClass modal = new TransactionClass();
        modal.setAmount(amount.trim());
        modal.setType(expense.trim());
        modal.setNote(note.trim());
        modal.setCategory(category.trim());
        modal.setDateWithTime(dateWithTime);
        LocalDate date = Instant.ofEpochMilli(dateWithTime)
                .atZone(ZoneId.systemDefault()) // default zone
                .toLocalDate();
        modal.setDate(Long.parseLong(date.toString().replace("-", "")));


        //   Log.d("date in local date" ,d.toString());
        transactionRepository.insert(modal);
        EventBus.getDefault().post(new EventMessage(6, "success"));
        //  liveData.postValue("something");

    }

    public void saveSelectedDate(Long date) {
        this.date = date;
    }

    public Long getSelectedDate() {
        return date;
    }

    public LiveData<List<TransactionClass>> getAllTransactions() {
        return transactionRepository.getAllTransactions();
    }

    public void validateCategoryData(CategoryClass categoryClass) {
        if (categoryClass.categoryName.isEmpty()) {
            // textInputLayoutName.setError("Name field can't be empty");
            EventBus.getDefault().post(new EventMessage(1, "Name field can't be empty"));
        } else if (categoryClass.categoryName.length() < 3) {
            //textInputLayoutName.setError("Name must have at least 3 letters");
            EventBus.getDefault().post(new EventMessage(12, "Name must have at least 3 letters"));
        } else if (categoryClass.categoryType.isEmpty()) {
            EventBus.getDefault().post(new EventMessage(2, "Please select a category type"));
        } else {
            storeCategoryInDatabase(categoryClass);
        }
    }

    private void storeCategoryInDatabase(CategoryClass categoryClass) {
        if(categoryClass.id == 0){
            //new category insert
            categoryRepository.insert(categoryClass);
        }else {
            //existing category update
            categoryRepository.update(categoryClass);
        }

        EventBus.getDefault().post(new EventMessage(3, "closing bottom sheet"));
    }

    public void fillCategoriesInDatabase(ArrayList<CategoryClass> categoryClassesList) {
        for (CategoryClass categoryClass : categoryClassesList) {
            categoryRepository.insert(categoryClass);
        }
        EventBus.getDefault().post(new EventMessage(9, "success"));
        Log.d("Chips", "completed again");
    }

    //to delete categories from database
    public void deleteCategoryFromDatabase(CategoryClass categoryClass){
        categoryRepository.delete(categoryClass);
    }

}
