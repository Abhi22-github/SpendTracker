package com.example.expensetracker.CalenderViewPager

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.Model.TransactionClass

abstract class OnlyDayAdapter(
    val context: Context,
    val transactionClasses: MutableList<TransactionClass>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return transactionClasses.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder,transactionClasses,position)
    }
    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder,transactionClasses: MutableList<TransactionClass>,position: Int)


}