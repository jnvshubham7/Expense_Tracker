package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.expensetracker.models.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import io.realm.Realm;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class Transaction_Activity extends AppCompatActivity {

    Realm realm;
    ArrayList<String> categories;
    ArrayAdapter adapter;
    Spinner cat;
    private String categorySelected;
    private EditText amount;
    private EditText memo;
    private TextView date;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Transaction Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();
        cat = findViewById(R.id.spinner_category);
        Category_Activity helper = new Category_Activity();
        progressBar = findViewById(R.id.progress_bar);
        categories = helper.getCategoryName();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat.setAdapter(adapter);


        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected = categories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        amount = findViewById(R.id.trans_amt);
        date = findViewById(R.id.date);
        Date date2 = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = simpleDateFormat.format(date2);
        date.setText(formattedDate);
        memo = findViewById(R.id.memo);
        Button saveButton = findViewById(R.id.btn_details);
        saveButton.setOnClickListener(view -> {
            if (!amount.getText().toString().isEmpty()){
                addTransactionToDatabse(categorySelected, amount.getText().toString(), date.getText().toString(),
                        memo.getText().toString());
                finish();
            }

        });


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void addTransactionToDatabse(String category, String amount, String date, String memo){
        progressBar.setVisibility(View.VISIBLE);
        String Id = "TRA" + Calendar.getInstance().getTimeInMillis();
        realm.beginTransaction();
        Transaction transaction = realm.createObject(Transaction.class, Id);
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setDate(date);
        transaction.setMemo(memo);
        realm.commitTransaction();
        progressBar.setVisibility(View.GONE);
    }
}
