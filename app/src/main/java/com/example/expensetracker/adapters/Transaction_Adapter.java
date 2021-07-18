package com.example.expensetracker.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expensetracker.R;

import com.example.expensetracker.models.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Transaction_Adapter extends RecyclerView.Adapter<Transaction_Adapter.ViewHolder>{

    private final ArrayList<Transaction> transactionArrayList;

    public Transaction_Adapter(Context context, ArrayList<Transaction> categoryArrayList) {
        this.transactionArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Transaction transaction = transactionArrayList.get(i);
        viewHolder.expenseCategory.setText(transaction.getCategory());
        viewHolder.expenseAmount.setText(transaction.getAmount());
        Date date2 = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = simpleDateFormat.format(date2);
        viewHolder.dateTrans.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return transactionArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView expenseCategory;
        private final TextView expenseAmount;
        private final TextView dateTrans;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseCategory = itemView.findViewById(R.id.category_trans);
            expenseAmount = itemView.findViewById(R.id.amount_text);
            dateTrans = itemView.findViewById(R.id.date_transaction);

        }
    }
}
