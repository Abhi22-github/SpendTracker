package com.roaa.expensetracker.Hilt

import android.content.Context
import com.roaa.expensetracker.Database.CategoryRepository
import com.roaa.expensetracker.Database.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule(){
    @Provides
    fun provideTransactionRepository(@ApplicationContext applicationContext: Context): TransactionRepository {
        return TransactionRepository(applicationContext)
    }

    @Provides
    fun provideCategoryRepository(@ApplicationContext applicationContext: Context): CategoryRepository {
        return CategoryRepository(applicationContext)
    }
}