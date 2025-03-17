package com.roaa.expensetracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.roaa.expensetracker.model.DailyBalancesClass

@Dao
interface DailyBalanceDao {
    //Normal
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dailyBalancesClass: DailyBalancesClass): Long

    @Delete
    suspend fun delete(dailyBalancesClass: DailyBalancesClass)

    @Update
    suspend fun update(dailyBalancesClass: DailyBalancesClass)

}
