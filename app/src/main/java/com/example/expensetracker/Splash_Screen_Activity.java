package com.example.expensetracker;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;

public class Splash_Screen_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StatusBarUtil.setTransparent(this);
        Handler handler = new Handler();
        Runnable runnable = () -> {
            Intent intent = new Intent(Splash_Screen_Activity.this, Login_Activity.class);
            startActivity(intent);
        };
        handler.postDelayed(runnable, 500);
    }
}
