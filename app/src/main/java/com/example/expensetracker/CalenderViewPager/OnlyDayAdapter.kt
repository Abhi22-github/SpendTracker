package com.example.expensetracker.CalenderViewPager

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.Model.TransactionClass

abstract class OnlyDayAdapter(
    val context: Context,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var transactionClasses: MutableList<TransactionClass> = mutableListOf()

    fun updateData(transactionClasses: MutableList<TransactionClass>){
        this.transactionClasses = transactionClasses
    }
    override fun getItemCount(): Int {
        return transactionClasses.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder,transactionClasses,position)
    }
    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder,transactionClasses: MutableList<TransactionClass>,position: Int)


}