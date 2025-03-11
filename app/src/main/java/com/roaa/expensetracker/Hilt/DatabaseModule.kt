package com.roaa.expensetracker.Hilt

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.roaa.expensetracker.Database.AppDatabase
import com.roaa.expensetracker.Database.AppDatabase.Companion.MIGRATION_1_2
import com.roaa.expensetracker.Database.AppDatabase.Companion.prePopulateData
import com.roaa.expensetracker.Database.BankAccountDao
import com.roaa.expensetracker.Database.BudgetDao
import com.roaa.expensetracker.Database.BudgetDayDao
import com.roaa.expensetracker.Database.CategoryDao
import com.roaa.expensetracker.Database.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule() {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "database1"
        )
            .addMigrations(MIGRATION_1_2)
            .addCallback(prePopulateData).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    @Singleton
    fun provideBankAccountDao(database: AppDatabase): BankAccountDao {
        return database.bankAccountsDao()
    }

    @Provides
    @Singleton
    fun provideBudgetDao(database: AppDatabase): BudgetDao {
        return database.budgetDao()
    }


    @Provides
    @Singleton
    fun provideBudgetDayDao(database: AppDatabase): BudgetDayDao {
        return database.budgetDayDao()
    }
}