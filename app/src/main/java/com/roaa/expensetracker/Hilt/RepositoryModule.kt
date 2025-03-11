package com.roaa.expensetracker.Hilt


import com.roaa.expensetracker.Database.BankAccountDao
import com.roaa.expensetracker.Database.BankAccountRepository
import com.roaa.expensetracker.Database.BudgetDao
import com.roaa.expensetracker.Database.BudgetDayDao
import com.roaa.expensetracker.Database.BudgetDayRepository
import com.roaa.expensetracker.Database.BudgetRepository
import com.roaa.expensetracker.Database.CategoryDao
import com.roaa.expensetracker.Database.CategoryRepository
import com.roaa.expensetracker.Database.TransactionDao
import com.roaa.expensetracker.Database.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule() {
    @Provides
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
        return TransactionRepository(transactionDao)
    }

    @Provides
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }

    @Provides
    fun provideBankAccountRepository(bankAccountDao: BankAccountDao): BankAccountRepository {
        return BankAccountRepository(bankAccountDao)
    }

    @Provides
    fun provideBudgetRepository(budgetDao: BudgetDao): BudgetRepository {
        return BudgetRepository(budgetDao)
    }

    @Provides
    fun provideBudgetDayRepository(budgetDayDao: BudgetDayDao): BudgetDayRepository {
        return BudgetDayRepository(budgetDayDao)
    }
}