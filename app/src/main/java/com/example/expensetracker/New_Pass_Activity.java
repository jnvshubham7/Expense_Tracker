package com.example.expensetracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class New_Pass_Activity extends AppCompatActivity {

    public EditText emailId;
    Button btnNewPass;
    public FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpass_activity);
        emailId = findViewById(R.id.emailAdd);
        btnNewPass = findViewById(R.id.newPassLink);

        firebaseAuth = FirebaseAuth.getInstance();


        btnNewPass.setOnClickListener(v -> {
            String email = emailId.getText().toString();

            if(TextUtils.isEmpty(email)){
                Toast.makeText(getApplicationContext(),"Please fill e-mail",Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Password reset link was sent your email address",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Mail sending error",Toast.LENGTH_SHORT).show();
                        }
                    });
        });


    }
}