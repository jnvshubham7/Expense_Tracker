package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.helpers.Preferences_Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.jaeger.library.StatusBarUtil;

import java.util.Objects;

import io.realm.Realm;

public class Main_Activity extends AppCompatActivity {
    public EditText emailId, passwd, confPasswd, username;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;

    private Preferences_Util SP;

    private ProgressBar signUpProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Realm.init(getApplicationContext());
        SP = Preferences_Util.getInstance(this);
        StatusBarUtil.setTransparent(this);
        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.ETemail);
        passwd = findViewById(R.id.ETpassword);
        username = findViewById(R.id.username_edittext);
        btnSignUp = findViewById(R.id.btnSignUp);
        signUpProgress = findViewById(R.id.signup_progressbar);
        signIn = findViewById(R.id.TVSignIn);
        confPasswd = findViewById(R.id.Confpassword);

        btnSignUp.setOnClickListener(view -> {
            signUpProgress.setVisibility(View.VISIBLE);
            String emailID = emailId.getText().toString();
            String paswd = passwd.getText().toString();
            String confPd = confPasswd.getText().toString();
            SharedPreferences.Editor editor = SP.getEditor();
            editor.putString("emailID", emailID);
            editor.putString("username", username.getText().toString());
            editor.commit();
            if (emailID.isEmpty()) {
                emailId.setError("Provide your Email first!");
                emailId.requestFocus();
            }
            else if (paswd.isEmpty()) {
                passwd.setError("Set your password");
                passwd.requestFocus();
            }
            else if (!paswd.equals(confPd)) {
                Toast.makeText(Main_Activity.this, "Both password fields must be identical",
                        Toast.LENGTH_SHORT).show();
            }
            else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(Main_Activity.this, (OnCompleteListener) task -> {
                    signUpProgress.setVisibility(View.GONE);
                    if (!task.isSuccessful()) {
                        Toast.makeText(Main_Activity.this.getApplicationContext(),
                                "SignUp unsuccessful: " + Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(Main_Activity.this, User_Activity.class));
                    }
                });
            }
            else {
                Toast.makeText(Main_Activity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        signIn.setOnClickListener(view -> {
            Intent I = new Intent(Main_Activity.this, Login_Activity.class);
            startActivity(I);
        });
    }
}