package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jaeger.library.StatusBarUtil;

public class Login_Activity extends AppCompatActivity {
    public EditText loginEmailId, logInpasswd;
    Button btnLogIn;
    TextView signup, newPassButton;
    private FirebaseAuth.AuthStateListener authStateListener;

    private static final int RC_SIGN_IN = 234;
    private static final String TAG = "example";

    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logingin);
        StatusBarUtil.setTransparent(this);
        firebaseAuth = FirebaseAuth.getInstance();
        loginEmailId = findViewById(R.id.loginEmail);
        loginEmailId.setHighlightColor(getResources().getColor(R.color.colorPrimary));
        logInpasswd = findViewById(R.id.loginpaswd);
        btnLogIn = findViewById(R.id.btnLogIn);
        signup = findViewById(R.id.TVSignIn);
        newPassButton = findViewById(R.id.forgotPass);

        newPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Activity.this, New_Pass_Activity.class));
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(view -> signIn());


        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Toast.makeText(Login_Activity.this, "User logged in ", Toast.LENGTH_SHORT).show();
                Intent I = new Intent(Login_Activity.this, User_Activity.class);
                startActivity(I);
            }


        };

        signup.setOnClickListener(view -> {
            Intent I = new Intent(Login_Activity.this, Main_Activity.class);
            startActivity(I);
        });

        btnLogIn.setOnClickListener(view -> {
            String userEmail = loginEmailId.getText().toString();
            String userPaswd = logInpasswd.getText().toString();
            if (userEmail.isEmpty()) {
                loginEmailId.setError("Provide your Email first!");
                loginEmailId.requestFocus();
            }
            else if (userPaswd.isEmpty()) {
                logInpasswd.setError("Enter Password!");
                logInpasswd.requestFocus();
            }
            else {
                firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(Login_Activity.this, (OnCompleteListener) task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(Login_Activity.this, "Not successfull", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startActivity(new Intent(Login_Activity.this, User_Activity.class));
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, User_Activity.class));
        }
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {


            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);


                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);


        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        Toast.makeText(Login_Activity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                    } else {

                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(Login_Activity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }

                });
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



}