package com.example.quemefaltaapp.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quemefaltaapp.MainActivity;
import com.example.quemefaltaapp.R;
import com.example.quemefaltaapp.classes.SessionManager;
import com.example.quemefaltaapp.classes.User;
import com.example.quemefaltaapp.helpers.AuthenticationHelper;
import com.example.quemefaltaapp.helpers.DatabaseHelper;
import com.example.quemefaltaapp.helpers.Helpers;
import com.example.quemefaltaapp.helpers.LocalStorageHelper;
import com.example.quemefaltaapp.interfaces.OnResultListener;
import com.example.quemefaltaapp.interfaces.OnUserResultListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    TextInputEditText etEmail, etPass;
    TextView tvRegisterHere, tvRecoverPass;
    Button btnLogin;
    AuthenticationHelper authHelper;
    Helpers helper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = SessionManager.getInstance(this);

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
        // Valida entrada de campos de texto
        if(validateLogin(email, pass)){
            // Autentifica si usuario existe y si la contraseña es correcta
            authHelper.LoginUserAuth(email, pass, new OnResultListener() {
                @Override
                public void onResultSuccess() {
                    //En caso de estar autentificado, busca los datos en la BD del usuario
                    FirebaseUser userAuth = authHelper.getUser();
                    String uid = userAuth.getProviderData().get(0).getUid();
                    String username = userAuth.getProviderData().get(0).getEmail();
                    sessionManager.loginUser(uid,username);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                }

                @Override
                public void onResultFailure(String errorMessage) {
                    Toast.makeText(LoginActivity.this, "Error:" + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }

    }


    private boolean validateLogin(String email, String pass){
        boolean valid = true;
        if (TextUtils.isEmpty(email)){
            etEmail.setError("El email no puede estar vacio");
            etEmail.requestFocus();
            valid = false;
        } else if(!helper.isValidEmail(email)){
            etEmail.setError("El email no tiene un formato válido");
            etEmail.requestFocus();
            valid = false;
        }
        if (TextUtils.isEmpty(pass)){
            etPass.setError("La contraseña no puede estar vacia");
            etPass.requestFocus();
            valid = false;
        }
        return valid;
    }
}