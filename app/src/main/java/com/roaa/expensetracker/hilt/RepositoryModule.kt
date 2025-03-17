package com.roaa.expensetracker.hilt


import com.roaa.expensetracker.database.BankAccountDao
import com.roaa.expensetracker.database.BankAccountRepository
import com.roaa.expensetracker.database.BudgetDao
import com.roaa.expensetracker.database.BudgetDayDao
import com.roaa.expensetracker.database.BudgetDayRepository
import com.roaa.expensetracker.database.BudgetRepository
import com.roaa.expensetracker.database.CategoryDao
import com.roaa.expensetracker.database.CategoryRepository
import com.roaa.expensetracker.database.DailyBalanceDao
import com.roaa.expensetracker.database.DailyBalanceRepository
import com.roaa.expensetracker.database.TransactionDao
import com.roaa.expensetracker.database.TransactionRepository
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
    fun provideBankAccountDailyBalanceRepository(dailyBalanceDao: DailyBalanceDao): DailyBalanceRepository {
        return DailyBalanceRepository(dailyBalanceDao)
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