package com.roaa.expensetracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.roaa.expensetracker.Events.EventMessage;
import com.roaa.expensetracker.Model.CategoryClass;
import com.roaa.expensetracker.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CategoryExpenseViewChipAdapter extends RecyclerView.Adapter<CategoryExpenseViewChipAdapter.CategoryViewHolder> {
    Context context;
    List<CategoryClass> categoryClassList;
    Chip lastSelectedChip,currentSelectedChip;

    public CategoryExpenseViewChipAdapter(Context context, List<CategoryClass> categoryClassList){
        this.context = context;
        this.categoryClassList = categoryClassList;
        lastSelectedChip = new Chip(context);
    }

    @NonNull
    @Override
    public CategoryExpenseViewChipAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_chip_layout,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryExpenseViewChipAdapter.CategoryViewHolder holder, int position) {
        holder.chip.setText(categoryClassList.get(position).categoryName);
        holder.chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectedChip = holder.chip;
                lastSelectedChip.setChecked(false);
                lastSelectedChip = currentSelectedChip;
                EventBus.getDefault().post(new EventMessage(7,currentSelectedChip.getText().toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryClassList.size();
    }
    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private Chip chip;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip_category_singleChipLayout);
        }
    }
}
