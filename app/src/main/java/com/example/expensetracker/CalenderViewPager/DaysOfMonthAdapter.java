package com.example.expensetracker.CalenderViewPager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.Model.DateWithAmountClass;
import com.example.expensetracker.Model.Day;

import java.util.ArrayList;

public abstract class DaysOfMonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<DateWithAmountClass> daysList;

    DaysOfMonthAdapter(Context context, ArrayList<DateWithAmountClass> daysList) {
        this.context = context;
        this.daysList = daysList;
    }

    public void updateData(ArrayList<DateWithAmountClass> daysList){
        this.daysList = daysList;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder(holder, daysList.get(holder.getLayoutPosition()));
    }



    //42 cell grid
    @Override
    public int getItemCount() {
        return (7 * 6);
    }

    abstract void onBindViewHolder(RecyclerView.ViewHolder holder, DateWithAmountClass totalAmountClass);


}



