package com.example.expensetracker.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.expensetracker.Model.CategoryClass;
import com.example.expensetracker.Model.TransactionClass;

@Database(entities = {TransactionClass.class, CategoryClass.class}, version = 1)
public abstract class TransactionDatabase extends RoomDatabase {

    // below line is to create instance
    // for our database class.
    private static TransactionDatabase instance;

    // below line is to create
    // abstract variable for dao.
    public abstract TransactionDao transactionDao();
    public abstract CategoryDao categoryDao();

    // on below line we are getting instance for our database.
    public static synchronized TransactionDatabase getInstance(Context context) {
        // below line is to check if
        // the instance is null or not.
        if (instance == null) {
            // if the instance is null we
            // are creating a new instance
            instance =
                    // for creating a instance for our database
                    // we are creating a database builder and passing
                    // our database class with our database name.
                    Room.databaseBuilder(context.getApplicationContext(),
                                    TransactionDatabase.class, "transaction_database")
                            // below line is use to add fall back to
                            // destructive migration to our database.
                            .fallbackToDestructiveMigration()
                            // below line is to add callback
                            // to our database.
                            .addCallback(roomCallback)
                            .allowMainThreadQueries()
                            // below line is to
                            // build our database.
                            .build();
        }
        // after creating an instance
        // we are returning our instance
        return instance;
    }

    // below line is to create a callback for our room database.
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // this method is called when database is created
            // and below line is to populate our data.
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    // we are creating an async task class to perform task in background.
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(TransactionDatabase instance) {
            TransactionDao transactionDao = instance.transactionDao();
            CategoryDao categoryDao = instance.categoryDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
