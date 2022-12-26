package com.halim.flixflex.Connection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.halim.flixflex.ClassesUtils.LoadingDialog;
import com.halim.flixflex.ClassesUtils.StaticMethods;
import com.halim.flixflex.MainActivity;
import com.halim.flixflex.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText email_input;
    private TextInputEditText passwordInput;
    private TextView btnConnection;
    private TextView btnSignup;
    private TextView Text2;
    private View divider1;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        findViewsById();

        btnConnection.setVisibility(View.GONE);
        divider1.setVisibility(View.GONE);
        Text2.setVisibility(View.GONE);

        //Create our Firebase instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(view -> {
            registerUser(email_input.getText().toString().trim(), passwordInput.getText().toString().trim());
        });

    }

    private void findViewsById() {
        email_input = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.passwordInput);
        btnConnection = findViewById(R.id.btnConnection);
        btnSignup = findViewById(R.id.btnSignup);
        Text2 = findViewById(R.id.Text2);
        divider1 = findViewById(R.id.divider1);
    }

    //Methode to insert user data into Firebase
    private void registerUser(String email, String password) {

        if (StaticMethods.validateEmail(email)) {
            if (StaticMethods.checkPassword(password)) {
                LoadingDialog loadingDialog = new LoadingDialog(RegisterActivity.this);
                loadingDialog.show();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        loadingDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Signup Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, getString(R.string.password_must_contains), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.email_not_valid), Toast.LENGTH_SHORT).show();
        }
    }

}