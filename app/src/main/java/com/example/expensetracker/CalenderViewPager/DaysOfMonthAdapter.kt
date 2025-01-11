package com.example.expensetracker.CalenderViewPager

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

abstract class DaysOfMonthAdapter internal constructor(
    private val context: Context,
    private val daysList: ArrayList<LocalDate>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var map: HashMap<Long, Pair<Long, Long>>

    init {
        map = hashMapOf()
    }

    fun updateData(map: HashMap<Long, Pair<Long, Long>>) {
        this.map = map
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, daysList[holder.layoutPosition], map)
    }


    //42 cell grid
    override fun getItemCount(): Int {
        return (7 * 6)
    }

    abstract fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        date: LocalDate,
        map: HashMap<Long, Pair<Long, Long>>
    )
}



