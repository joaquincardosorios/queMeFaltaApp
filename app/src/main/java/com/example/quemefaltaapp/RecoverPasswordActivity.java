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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPasswordActivity extends AppCompatActivity {

    TextInputEditText etEmail;
    TextView tvLogin;
    Button btnReset;
    Helpers helper;
    AuthenticationHelper authHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        etEmail = findViewById(R.id.etEmailRecoverPass);
        tvLogin = findViewById(R.id.tvLoginRecoverPass);
        btnReset = findViewById(R.id.btnResetPass);

        helper = new Helpers();
        authHelper = new AuthenticationHelper();

        btnReset.setOnClickListener( view -> {
            resetPassword();
        });
        tvLogin.setOnClickListener( view ->{
            startActivity(new Intent(RecoverPasswordActivity.this, LoginActivity.class ));
        });
    }

    private void resetPassword(){
        String email = etEmail.getText().toString();
        if(TextUtils.isEmpty(email)){
            etEmail.setError("El correo no puede estar vacio");
            etEmail.requestFocus();
        } else if(!helper.isValidEmail(email)){
            etEmail.setError("El formato del correo no es válido");
            etEmail.requestFocus();
        } else{
            authHelper.ResetPassAuth(this, email);
        }
    }
}