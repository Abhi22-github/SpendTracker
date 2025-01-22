package com.roaa.expensetracker.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.Model.TransactionClass

@Database(entities = [TransactionClass::class, CategoryClass::class], version = 1)
abstract class TransactionDatabase : RoomDatabase() {
    // below line is to create
    // abstract variable for dao.
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

//    // we are creating an async task class to perform task in background.
//    private class PopulateDbAsyncTask(instance: TransactionDatabase) :
//        AsyncTask<Void?, Void?, Void?>() {
//        init {
//            val transactionDao = instance.transactionDao()
//            val categoryDao = instance.categoryDao()
//        }
//
//        override fun doInBackground(vararg voids: Void): Void? {
//            return null
//        }
//    }

    companion object {
        // below line is to create instance
        // for our database class.
        private var instance: TransactionDatabase? = null

        // on below line we are getting instance for our database.
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): TransactionDatabase {
            // below line is to check if
            // the instance is null or not.
            if (instance == null) {
                // if the instance is null we
                // are creating a new instance
                instance =  // for creating a instance for our database
                        // we are creating a database builder and passing
                        // our database class with our database name.
                    databaseBuilder(
                        context.applicationContext,
                        TransactionDatabase::class.java, "transaction_database"
                    ) // below line is use to add fall back to
                        // destructive migration to our database.
                        .fallbackToDestructiveMigration() // below line is to add callback
                        // to our database.
                      //  .addCallback(roomCallback)
                        .allowMainThreadQueries() // below line is to
                        // build our database.
                        .build()
            }
            // after creating an instance
            // we are returning our instance
            return instance!!
        }

        // below line is to create a callback for our room database.
//        private val roomCallback: Callback = object : Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                // this method is called when database is created
//                // and below line is to populate our data.
//                PopulateDbAsyncTask(instance!!).execute()
//            }
//        }
    }
}
