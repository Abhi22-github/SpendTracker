package com.roaa.expensetracker.database.databaseUtils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        // 🛠 Add new columns
        database.execSQL("ALTER TABLE bank_accounts ADD COLUMN accountAddedTimestamp INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE bank_accounts ADD COLUMN balanceLastUpdatedTimeStamp INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE bank_balance_corrections ADD COLUMN addedTimeStamp INTEGER NOT NULL DEFAULT 0")


        // 🛠 Update existing records with a valid timestamp
        val currentTime = System.currentTimeMillis()
        database.execSQL("UPDATE bank_accounts SET accountAddedTimestamp = $currentTime WHERE accountAddedTimestamp = 0")
        database.execSQL("UPDATE bank_accounts SET balanceLastUpdatedTimeStamp = $currentTime WHERE balanceLastUpdatedTimeStamp = 0")
        database.execSQL("UPDATE bank_balance_corrections SET addedTimeStamp = $currentTime WHERE addedTimeStamp = 0")


    }
}

