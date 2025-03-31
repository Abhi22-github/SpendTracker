package com.roaa.expensetracker.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.roaa.expensetracker.database.databaseUtils.RoomConverters
import com.roaa.expensetracker.model.BankAccountsClass
import com.roaa.expensetracker.model.BankAmountCorrectionsClass
import com.roaa.expensetracker.model.BudgetDayModelClass
import com.roaa.expensetracker.model.BudgetModelClass
import com.roaa.expensetracker.model.CategoryClass
import com.roaa.expensetracker.model.TransactionClass


@TypeConverters(RoomConverters::class)
@Database(
    entities = [TransactionClass::class, CategoryClass::class, BankAccountsClass::class, BudgetModelClass::class, BudgetDayModelClass::class, BankAmountCorrectionsClass::class],
    version = 1,
    exportSchema = true,
    autoMigrations = [
    ]
)
abstract class AppDatabase : RoomDatabase() {

    // below line is to create
    // abstract variable for dao.
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun bankAccountsDao(): BankAccountDao
    abstract fun budgetDao(): BudgetDao
    abstract fun budgetDayDao(): BudgetDayDao
    abstract fun dailyBalanceDao(): BankAccountCorrectionsDao

    companion object {
        // below line is to create a callback for our room database.
        val prePopulateData: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // this method is called when database is created
                // and below line is to populate our data.
                //expenses
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Bills',1,26,'Expense',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('EMI',1,27,'Expense',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Education',1,20,'Expense',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Entertainment',1,17,'Expense',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Food & Drinks',1,1,'Expense',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Groceries',1,18,'Expense',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Health',1,14,'Expense',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Housing',1,10,'Expense',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Other',1,99,'Expense',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Personal',1,28,'Expense',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Transportation',1,15,'Expense',1) ")

                //income
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Awards',1,29,'Income',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Coupons',1,30,'Income',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Refunds',1,22,'Income',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Rent',1,10,'Income',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Salary',1,32,'Income',1) ")
                db.execSQL("INSERT INTO category_table(categoryName, categoryColorNumber, categoryIconNumber, categoryType,isActive) VALUES('Other',1,99,'Income',1) ")

                //payment method cash
                db.execSQL("INSERT INTO bank_accounts(initialAmount, currentAmount, bankName, accountAddedDate, balanceLastUpdatedDate,cardColorNumber,cardIconNumber,accountType,isActive) VALUES('0','0','Cash',20250101,20250101,1,25,'CASH',1) ")
            }
        }
    }
}
