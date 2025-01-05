package com.example.expensetracker.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.Actions;
import com.example.expensetracker.AddCategoryBottomSheet;
import com.example.expensetracker.Model.CategoryClass;
import com.example.expensetracker.R;
import com.example.expensetracker.Utilities.Constants;

import java.util.List;

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewAdapter.CategoryViewHolder> {
    private List<CategoryClass> categoryClassList;
    private Context context;
    private Actions actions;

    public CategoryViewAdapter(Context context, List<CategoryClass> categoryClassList) {
        this.categoryClassList = categoryClassList;
        this.context = context;
        this.actions = (Actions) context;
    }

    @NonNull
    @Override
    public CategoryViewAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category_view,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewAdapter.CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.categoryName.setText(categoryClassList.get(position).categoryName);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryBottomSheet addCategoryBottomSheet = new AddCategoryBottomSheet(categoryClassList.get(position));
                addCategoryBottomSheet.show(((AppCompatActivity)context).getSupportFragmentManager(), categoryClassList.get(position).categoryType);
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove the category from the list
                actions.onDeleteCategory(categoryClassList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryClassList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageButton editButton,removeButton;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.textView_categoryName_singleCategory);
            editButton = itemView.findViewById(R.id.imageButton_edit_addActivity);
            removeButton = itemView.findViewById(R.id.imageButton_remove_addActivity);
        }
    }
}
