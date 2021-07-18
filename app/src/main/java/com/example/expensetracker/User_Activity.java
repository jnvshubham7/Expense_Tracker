package com.example.expensetracker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.adapters.Transaction_Adapter;
import com.example.expensetracker.helpers.Preferences_Util;
import com.example.expensetracker.models.Category;
import com.example.expensetracker.models.Transaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class User_Activity extends AppCompatActivity {
    Button btnLogOut;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private RecyclerView recyclerView;

    private Realm realm;

    private TextView textView;

    private Preferences_Util SP;

    private TextView monthly_limit_Textview;
    private TextView balanceTextView;
    private TextView expensesTextView;

    private final int[] totalAmt = {0};

    private final int[] amount = {0};

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    public User_Activity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        recyclerView = findViewById(R.id.expense_item_view);
        SP = Preferences_Util.getInstance(this);
        textView = findViewById(R.id.gomn);
        monthly_limit_Textview = findViewById(R.id.montly_limit);
        balanceTextView = findViewById(R.id.balance);
        expensesTextView = findViewById(R.id.todays_expenses);
        Date date2 = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = simpleDateFormat.format(date2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setAdapter();
        FloatingActionButton floatingActionButton = findViewById(R.id.add_floating_button);
        floatingActionButton.setOnClickListener(view -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(User_Activity.this);
            builder.setItems(getResources().getStringArray(R.array.add_array), (dialogInterface, i) -> {
                switch (i){
                    case 0:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(User_Activity.this);
                        View view1 = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
                        final EditText categoryName = view1.findViewById(R.id.category_name);
                        final EditText categoryLimit = view1.findViewById(R.id.category_limit);
                        builder1.setView(view1);
                        builder1.setPositiveButton("OK", (dialogInterface1, i1) -> {
                            if (categoryName.getText().toString().isEmpty() || categoryLimit.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(), "Enter valid info for new category", Toast.LENGTH_SHORT).show();
                            } else {
                                String category = categoryName.getText().toString();
                                String categoryId = category.substring(0, 3).toUpperCase();
                                Random random = new Random();
                                String randomno = String.valueOf(random.nextInt(99));
                                String categoryType = categoryLimit.getText().toString();
                                dialogInterface1.dismiss();
                                addCategoryInfo(categoryId+randomno, category, categoryType);
                            }
                            Intent intent = new Intent(User_Activity.this, Category_Activity.class);
                            startActivity(intent);
                        });

                        builder1.setNegativeButton("CANCEL", (dialogInterface12, i12) -> dialogInterface12.dismiss());
                        AlertDialog dialog = builder1.create();
                        dialog.show();

                        break;

                    case 1:
                        Intent in = new Intent(User_Activity.this, Transaction_Activity.class );
                        startActivity(in);
                        break;

                    case 2:
                        AlertDialog.Builder monthlyLimitDialog = new AlertDialog.Builder(User_Activity.this);
                        View view2 = getLayoutInflater().inflate(R.layout.dialog_add_limit, null);
                        final EditText monthlyLimit = view2.findViewById(R.id.monthly_limit);
                        monthlyLimitDialog.setView(view2);
                        monthlyLimitDialog.setPositiveButton("OK", (dialogInterface13, i13) -> {
                            if (monthlyLimit.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(), "Enter valid limit", Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPreferences.Editor editor = SP.getEditor();
                                editor.putString("monthly_limit", monthlyLimit.getText().toString());
                                editor.commit();
                            }
                            monthly_limit_Textview.setText(SP.getString("monthly_limit", "0"));
                        });
                        monthlyLimitDialog.setNegativeButton("CANCEL", (dialogInterface14, i14) -> dialogInterface14.dismiss());
                        AlertDialog dialog2 = monthlyLimitDialog.create();
                        dialog2.show();

                }

            });
            Dialog dialog = builder.create();
            dialog.show();
        });


    }

    private ArrayList<Transaction> getTransactionData(){
        final ArrayList<Transaction> transactionArrayList = new ArrayList<>();
        realm.executeTransaction(realm -> {
            RealmResults<Transaction> transactionRealmResults = realm.where(Transaction.class).findAll();
            if (transactionRealmResults.size() != 0){
                textView.setVisibility(View.GONE);
                for (Transaction t: transactionRealmResults){
                    amount[0] = amount[0] + Integer.parseInt(t.getAmount());
                    transactionArrayList.add(t);
                }
            } else {
                textView.setVisibility(View.VISIBLE);
            }
        });
        return transactionArrayList;
    }

    private void addCategoryInfo(final String id, final String name, final String categoryType) {
        realm.executeTransaction(realm -> {
            Category category = realm.createObject(Category.class, id);
            category.setCategoryName(name);
            category.setCategoryType(categoryType);
        });
    }

    private void setAdapter(){
        balanceTextView.setText(String.valueOf(Integer.parseInt(SP.getString("monthly_limit", "0")) - amount[0]));
        expensesTextView.setText(String.valueOf(amount[0]));
        Date date2 = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = simpleDateFormat.format(date2);
        Transaction_Adapter transactionAdapter = new Transaction_Adapter(this, getTransactionData());
        recyclerView.setAdapter(transactionAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                signout();
                return true;

            case R.id.user_info:
                AlertDialog.Builder monthlyLimitDialog = new AlertDialog.Builder(User_Activity.this);
                View view2 = getLayoutInflater().inflate(R.layout.user_info, null);
                String name = SP.getString("username", "none");
                String email = SP.getString("emailID", "none");
                String monthlylimit = SP.getString("monthly_limit", "0");
                TextView Uname = view2.findViewById(R.id.username);
                Uname.setText(name);
                TextView userEmail = view2.findViewById(R.id.email);
                userEmail.setText(email);
                TextView mothlylimit = view2.findViewById(R.id.monthly_limit_value);
                mothlylimit.setText(monthlylimit);
                monthlyLimitDialog.setView(view2);
                monthlyLimitDialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                AlertDialog dialog = monthlyLimitDialog.create();
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();
        Intent in = new Intent(User_Activity.this, Login_Activity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
        finish();
    }
}




