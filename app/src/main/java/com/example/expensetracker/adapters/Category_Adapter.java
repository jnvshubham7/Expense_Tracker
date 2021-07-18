package com.example.expensetracker.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expensetracker.R;


import com.example.expensetracker.models.Category;

import java.util.ArrayList;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder>{

    private final ArrayList<Category> categoryArrayList;

    public Category_Adapter(Context context, ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Category category = categoryArrayList.get(i);
        viewHolder.expenseCategory.setText(category.getCategoryName());
        viewHolder.expenseType.setText(category.getCategoryType());
        viewHolder.expenseCategory.setTag(category);

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView expenseCategory;
        private final TextView expenseType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseCategory = itemView.findViewById(R.id.category_name);
            expenseType = itemView.findViewById(R.id.expense_cat);
        }
    }
}
