package com.halim.flixflex.Connection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.halim.flixflex.ClassesUtils.LoadingDialog;
import com.halim.flixflex.MainActivity;
import com.halim.flixflex.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email_input;
    private TextInputEditText passwordInput;
    private TextView btnConnection;
    private TextView btnSignup;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        findViewsById();

        //Create our instance Firebase
        auth = FirebaseAuth.getInstance();

        //Sign up button for user
        btnSignup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        //Login button for user
        btnConnection.setOnClickListener(view -> {
            loginUser(email_input.getText().toString().trim(), passwordInput.getText().toString().trim());
        });

    }

    private void findViewsById() {
        email_input = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.passwordInput);
        btnConnection = findViewById(R.id.btnConnection);
        btnSignup = findViewById(R.id.btnSignup);
    }

    //Login method, that check if user insert a valid Email and password
    private void loginUser(String email, String password) {
        if (!email.isEmpty()) {
            if (!password.isEmpty()) {
                LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
                loadingDialog.show();
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loadingDialog.dismiss();
                        Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    loadingDialog.dismiss();
                    Toast.makeText(LoginActivity.this, getString(R.string.oops_an_error_has_occurred), Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(this, getString(R.string.empty_password), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.empty_email), Toast.LENGTH_SHORT).show();
        }
    }
}