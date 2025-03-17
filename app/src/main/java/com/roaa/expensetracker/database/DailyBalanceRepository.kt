package com.roaa.expensetracker.database

import com.roaa.expensetracker.model.DailyBalancesClass
import javax.inject.Inject

class DailyBalanceRepository @Inject constructor(private val dailyBalanceDao: DailyBalanceDao) {


    // creating a method to insert the data to our database. 
    suspend fun insert(dailyBalancesClass: DailyBalancesClass) {
        dailyBalanceDao.insert(dailyBalancesClass)
    }

    // creating a method to update data in database. 
    suspend fun update(dailyBalancesClass: DailyBalancesClass) {
        dailyBalanceDao.update(dailyBalancesClass)
    }

    // creating a method to delete the data in our database. 
    suspend fun delete(dailyBalancesClass: DailyBalancesClass) {
        dailyBalanceDao.delete(dailyBalancesClass)
    }


}
