package com.example.quemefaltaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText etEmail, etPass;
    TextView tvRegisterHere, tvRecoverPass;
    Button btnLogin;
    AuthenticationHelper authHelper;
    Helpers helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmailLog);
        etPass = findViewById(R.id.etPassLog);
        tvRegisterHere = findViewById(R.id.tvRegistrarAqui);
        tvRecoverPass = findViewById(R.id.tvRecuperarPass);
        btnLogin = findViewById(R.id.btnLogin);

        authHelper = new AuthenticationHelper();
        helper = new Helpers();

        btnLogin.setOnClickListener(view -> {
            loginUser();
        });

        tvRegisterHere.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        tvRecoverPass.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RecoverPasswordActivity.class));
        });

    }

    private void loginUser(){
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

        if (TextUtils.isEmpty(email)){
            etEmail.setError("El email no puede estar vacio");
            etEmail.requestFocus();
        } else if(!helper.isValidEmail(email)){
            etEmail.setError("El email no tiene un formato válido");
            etEmail.requestFocus();
        }
        if (TextUtils.isEmpty(pass)){
            etPass.setError("La contraseña no puede estar vacia");
            etPass.requestFocus();
        }else{
            authHelper.LoginUserAuth(this,email,pass);
        }

    }
}