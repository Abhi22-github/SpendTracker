package com.example.expensetracker.CalenderViewPager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import kotlin.Pair;

public abstract class DaysOfMonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<LocalDate> daysList;
    private HashMap<Long, Pair<Long,Long>> map;

    DaysOfMonthAdapter(Context context, ArrayList<LocalDate> daysList) {
        this.context = context;
        this.daysList = daysList;
    }

    public void updateData(HashMap<Long, Pair<Long, Long>> map){
        this.map = map;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder(holder, daysList.get(holder.getLayoutPosition()),map);
    }




    //42 cell grid
    @Override
    public int getItemCount() {
        return (7 * 6);
    }

    abstract void onBindViewHolder(RecyclerView.ViewHolder holder, LocalDate date, HashMap<Long, Pair<Long, Long>> map);


}



