package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.adapters.Category_Adapter;
import com.example.expensetracker.models.Category;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class Category_Activity extends AppCompatActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Categories");
        realm = Realm.getDefaultInstance();
        RecyclerView recyclerView = findViewById(R.id.category_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Category_Adapter categoryAdapter = new Category_Adapter(this, getCategoryData());
        recyclerView.setAdapter(categoryAdapter);
    }

    private void addCategoriesForFirstTimeLaunch(){
        Realm realm = Realm.getDefaultInstance();
        ArrayList<Category> categories = new ArrayList<>();
        Category c1 = new Category();
        c1.setCategoryName("Food");
        c1.setCategoryType("Expense");
        c1.setCategoryId("FOO13");
        categories.add(c1);
        Category c2 = new Category();
        c2.setCategoryName("Transportation");
        c2.setCategoryType("Expense");
        c2.setCategoryId("TRA23");
        categories.add(c2);
        Category c3 = new Category();
        c3.setCategoryName("Electricity");
        c3.setCategoryType("Expense");
        c3.setCategoryId("ELE45");
        categories.add(c3);
        Category c4 = new Category();
        c4.setCategoryName("Entertainment");
        c4.setCategoryType("Expense");
        c4.setCategoryId("ENT56");
        categories.add(c4);
        Category c5 = new Category();
        c5.setCategoryName("Bills");
        c5.setCategoryType("Expense");
        c5.setCategoryId("BIL65");
        categories.add(c5);
        Category c6 = new Category();
        c6.setCategoryName("Salary");
        c6.setCategoryType("Income");
        c6.setCategoryId("SAL25");
        categories.add(c6);
        for (final Category category: categories){
            realm.executeTransaction(realm1 -> {
                Category category1 = realm1.createObject(Category.class);
                category1.setCategoryId(category.getCategoryId());
                category1.setCategoryName(category.getCategoryName());
                category1.setCategoryType(category.getCategoryType());
            });
        }
    }

    private ArrayList<Category> getCategoryData(){
        final ArrayList<Category> categories = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(Category.class).findAll().size() == 0)
            addCategoriesForFirstTimeLaunch();
        realm.executeTransaction(realm1 -> {
            RealmResults<Category> realmResults = realm1.where(Category.class).findAll();
            categories.addAll(realmResults);
        });
        return categories;
    }


    public ArrayList<String> getCategoryName(){
        Realm realm = Realm.getDefaultInstance();
        ArrayList<String> categories = new ArrayList<>();
        RealmResults<Category> realmResults = realm.where(Category.class).findAll();
        for (Category category: realmResults){
            categories.add(category.getCategoryName());
        }
        return categories;
    }

}
