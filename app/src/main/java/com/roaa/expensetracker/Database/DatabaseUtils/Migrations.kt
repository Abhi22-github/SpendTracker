package com.roaa.expensetracker.Database.DatabaseUtils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


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

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // For example, adding a new column
        database.execSQL(
            """
            CREATE TABLE budget_table_temp (
                budgetId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                totalBudgetAmount Float NOT NULL,
                budgetAmountPerDay Float NOT NULL,
                budgetTotalDays Long NOT NULL,
                budgetStartDate Long NOT NULL,
                budgetEndDate Long NOT NULL,
                restDistributionType INTEGER NOT NULL,
                notificationForBudgetUsage Float NOT NULL,
                isActive INTEGER NOT NULL
            )
            """
        )

        // Step 2: Copy data from the old table to the new table
        database.execSQL(
            """
            INSERT INTO budget_table_temp (
                budgetId,
                totalBudgetAmount,
                budgetAmountPerDay,
                budgetTotalDays,
                budgetStartDate,
                budgetEndDate,
                restDistributionType,
                notificationForBudgetUsage,
                isActive
            )
            SELECT
                budgetId,
                CAST(budgetAmountForMonth AS Float), 
                CAST(budgetAmountPerDay AS Float),
                budgetTotalDays,
                budgetMonthStartDate,
                budgetMonthEndDate,
                1,
                CAST(20 AS Float),
                isActive
            FROM budget_table
            """
        )

        // Step 3: Drop the old table
        database.execSQL("DROP TABLE budget_table")

        // Step 4: Rename the new table to the original table name
        database.execSQL("ALTER TABLE budget_table_temp RENAME TO budget_table")

    }
}

