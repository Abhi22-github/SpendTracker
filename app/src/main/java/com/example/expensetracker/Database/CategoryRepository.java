package com.example.expensetracker.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.expensetracker.Model.CategoryClass;
import com.example.expensetracker.Utilities.Constants;

import java.util.List;

public class CategoryRepository {
    // below line is the create a variable  
    // for dao and list for all category. 
    private CategoryDao categoryDao;
    private LiveData<List<CategoryClass>> allCategories,allExpenseCategories,allIncomeCategories;

    // creating a constructor for our variables 
    // and passing the variables to it.

    public CategoryRepository(Application application) {
        TransactionDatabase database = TransactionDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategory();
        allExpenseCategories = categoryDao.getOnlyExpenseCategories(Constants.EXPENSE);
        allIncomeCategories = categoryDao.getOnlyIncomeCategories(Constants.INCOME);
    }

    // creating a method to insert the data to our database. 
    public void insert(CategoryClass model) {
        new CategoryRepository.InsertCourseAsyncTask(categoryDao).execute(model);
    }

    // creating a method to update data in database. 
    public void update(CategoryClass model) {
        new CategoryRepository.UpdateCourseAsyncTask(categoryDao).execute(model);
    }

    // creating a method to delete the data in our database. 
    public void delete(CategoryClass model) {
        new CategoryRepository.DeleteCourseAsyncTask(categoryDao).execute(model);
    }

    // below is the method to delete all the category. 
    public void deleteAllTransaction() {
        new CategoryRepository.DeleteAllCoursesAsyncTask(categoryDao).execute();
    }


    // below method is to read all category from database. 
    public LiveData<List<CategoryClass>> getAllCategories() {
        return allCategories;
    }

    // below method is to read only expenses category from database. 
    public LiveData<List<CategoryClass>> getOnlyExpenseCategories() {
        return allExpenseCategories;
    }

    // below method is to read only expenses category from database.
    public LiveData<List<CategoryClass>> getOnlyIncomeCategories() {
        return allIncomeCategories;
    }

    // we are creating a async task method to insert new course. 
    private static class InsertCourseAsyncTask extends AsyncTask<CategoryClass, Void, Void> {
        private CategoryDao categoryDao;

        private InsertCourseAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(CategoryClass... model) {
            // below line is use to insert our modal in dao. 
            categoryDao.insert(model[0]);
            return null;
        }
    }

    // we are creating a async task method to update our course. 
    private static class UpdateCourseAsyncTask extends AsyncTask<CategoryClass, Void, Void> {
        private CategoryDao categoryDao;

        private UpdateCourseAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(CategoryClass... models) {
            // below line is use to update 
            // our modal in dao. 
            categoryDao.update(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete course. 
    private static class DeleteCourseAsyncTask extends AsyncTask<CategoryClass, Void, Void> {
        private CategoryDao categoryDao;

        private DeleteCourseAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(CategoryClass... models) {
            // below line is use to delete  
            // our course modal in dao. 
            categoryDao.delete(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all category. 
    private static class DeleteAllCoursesAsyncTask extends AsyncTask<Void, Void, Void> {
        private CategoryDao categoryDao;
        private DeleteAllCoursesAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method 
            // to delete all category. 
            categoryDao.deleteAllCategory();
            return null;
        }
    }
}
