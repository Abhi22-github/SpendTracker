package com.roaa.expensetracker.Database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.roaa.expensetracker.Model.BankAccountsClass
import com.roaa.expensetracker.Model.BudgetDayModelClass
import com.roaa.expensetracker.Model.BudgetModelClass
import com.roaa.expensetracker.Model.CategoryClass
import com.roaa.expensetracker.Model.TransactionClass

@Database(
    entities = [TransactionClass::class, CategoryClass::class, BankAccountsClass::class, BudgetModelClass::class, BudgetDayModelClass::class],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3, spec = AppDatabase.AutoMigrationSpecVersion2To3::class),
        AutoMigration(from = 3, to = 4)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    @RenameColumn.Entries(
        RenameColumn(
            tableName = "budget_table",
            fromColumnName = "budgetAmountForMonth",
            toColumnName = "totalBudgetAmount"
        ),
        RenameColumn(
            tableName = "budget_table",
            fromColumnName = "budgetMonthStartDate",
            toColumnName = "budgetStartDate"
        ),
        RenameColumn(
            tableName = "budget_table",
            fromColumnName = "budgetMonthEndDate",
            toColumnName = "budgetEndDate"
        ),
    )
    @DeleteColumn.Entries(
        DeleteColumn(
            tableName = "budget_table",
            columnName = "currentMonthName"
        )
    )
    class AutoMigrationSpecVersion2To3 : AutoMigrationSpec

    // below line is to create
    // abstract variable for dao.
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun bankAccountsDao(): BankAccountDao
    abstract fun budgetDao(): BudgetDao
    abstract fun budgetDayDao(): BudgetDayDao

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
        private var instance: AppDatabase? = null

        // on below line we are getting instance for our database.
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            // below line is to check if
            // the instance is null or not.
            if (instance == null) {
                // if the instance is null we
                // are creating a new instance
                instance =  // for creating a instance for our database
                        // we are creating a database builder and passing
                        // our database class with our database name.
                    databaseBuilder(
                        context.applicationContext, AppDatabase::class.java, "database"
                    ) // below line is use to add fall back to
                        // destructive migration to our database.
                        .addMigrations(MIGRATION_1_2)
                        //.addMigrations(MIGRATION_2_3)// below line is to add callback
                        // to our database.
                        .addCallback(prePopulateData).allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        // below line is to
                        // build our database.
                        .build()
            }
            // after creating an instance
            // we are returning our instance
            return instance!!
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // For example, adding a new column
                database.execSQL(
                    """
            CREATE TABLE bank_accounts_temp (
                bankAccountId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                initialAmount Float NOT NULL, 
                currentAmount Float NOT NULL,
                bankName TEXT NOT NULL,
                cardColorNumber INTEGER NOT NULL,
                cardIconNumber INTEGER NOT NULL,
                accountType TEXT NOT NULL,
                isActive INTEGER NOT NULL
            )
            """
                )

                // Step 2: Copy data from the old table to the new table
                database.execSQL(
                    """
            INSERT INTO bank_accounts_temp (
                bankAccountId, 
                initialAmount, 
                currentAmount, 
                bankName, 
                cardColorNumber, 
                cardIconNumber, 
                accountType, 
                isActive
            )
            SELECT 
                bankAccountId, 
                CAST(initialAmount AS Float), 
                CAST(currentAmount AS Float), 
                bankName, 
                cardColorNumber, 
                cardIconNumber, 
                accountType, 
                isActive
            FROM bank_accounts
            """
                )

                // Step 3: Drop the old table
                database.execSQL("DROP TABLE bank_accounts")

                // Step 4: Rename the new table to the original table name
                database.execSQL("ALTER TABLE bank_accounts_temp RENAME TO bank_accounts")

            }
        }

        // below line is to create a callback for our room database.
        private val prePopulateData: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // this method is called when database is created
                // and below line is to populate our data.
                //expenses
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Bills',1,26,'Expense',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('EMI',1,27,'Expense',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Education',1,20,'Expense',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Entertainment',1,17,'Expense',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Food & Drinks',1,1,'Expense',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Groceries',1,18,'Expense',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Health',1,14,'Expense',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Housing',1,10,'Expense',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Other',1,99,'Expense',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Personal',1,28,'Expense',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Transportation',1,15,'Expense',true) ")

                //income
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Awards',1,29,'Income',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Coupons',1,30,'Income',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Refunds',1,22,'Income',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Rent',1,10,'Income',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Salary',1,32,'Income',true) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Other',1,99,'Income',true) ")

                //payment method cash
                db.execSQL("INSERT INTO bank_accounts(initialAmount, currentAmount, bankName, cardColorNumber,cardIconNumber,accountType,isActive) VALUES(0,0,'Cash',1,25,'CASH',true) ")
            }
        }
    }
}
