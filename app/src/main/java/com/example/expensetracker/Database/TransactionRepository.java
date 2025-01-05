package com.example.expensetracker.Database;

import android.app.Application;
import android.os.AsyncTask;
import android.os.TransactionTooLargeException;

import androidx.lifecycle.LiveData;

import com.example.expensetracker.Model.TotalAmountClass;
import com.example.expensetracker.Model.TransactionClass;

import java.util.List;

public class TransactionRepository {

    // below line is the create a variable  
    // for dao and list for all courses. 
    private TransactionDao transactionDao;
    private LiveData<List<TransactionClass>> allTransactions;

    // creating a constructor for our variables 
    // and passing the variables to it. 
    public TransactionRepository(Application application) {
        TransactionDatabase database = TransactionDatabase.getInstance(application);
        transactionDao = database.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
    }

    // creating a method to insert the data to our database. 
    public void insert(TransactionClass model) {
        new InsertCourseAsyncTask(transactionDao).execute(model);
    }

    // creating a method to update data in database. 
    public void update(TransactionClass model) {
        new UpdateCourseAsyncTask(transactionDao).execute(model);
    }

    // creating a method to delete the data in our database. 
    public void delete(TransactionClass model) {
        new DeleteCourseAsyncTask(transactionDao).execute(model);
    }

    public TotalAmountClass getTotalAmountByDateAndCategoryType(Long date,String categoryType){
        return transactionDao.getTotalAmountByDateAndCategoryType(date,categoryType);
    }

    // below is the method to delete all the courses. 
    public void deleteAllTransaction() {
        new DeleteAllCoursesAsyncTask(transactionDao).execute();
    }

    // below method is to read all the courses. 
    public LiveData<List<TransactionClass>> getAllTransactions() {
        return allTransactions;
    }

    // we are creating a async task method to insert new course. 
    private static class InsertCourseAsyncTask extends AsyncTask<TransactionClass, Void, Void> {
        private TransactionDao transactionDao;

        private InsertCourseAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;
        }

        @Override
        protected Void doInBackground(TransactionClass... model) {
            // below line is use to insert our modal in dao. 
            transactionDao.insert(model[0]);
            return null;
        }
    }

    // we are creating a async task method to update our course. 
    private static class UpdateCourseAsyncTask extends AsyncTask<TransactionClass, Void, Void> {
        private TransactionDao transactionDao;

        private UpdateCourseAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;
        }

        @Override
        protected Void doInBackground(TransactionClass... models) {
            // below line is use to update 
            // our modal in dao. 
            transactionDao.update(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete course. 
    private static class DeleteCourseAsyncTask extends AsyncTask<TransactionClass, Void, Void> {
        private TransactionDao transactionDao;

        private DeleteCourseAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;
        }

        @Override
        protected Void doInBackground(TransactionClass... models) {
            // below line is use to delete  
            // our course modal in dao. 
            transactionDao.delete(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all courses. 
    private static class DeleteAllCoursesAsyncTask extends AsyncTask<Void, Void, Void> {
        private TransactionDao transactionDao;
        private DeleteAllCoursesAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method 
            // to delete all courses. 
            transactionDao.deleteAllTransaction();
            return null;
        }
    }
}
