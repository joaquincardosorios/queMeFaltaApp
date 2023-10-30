package com.example.quemefaltaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterActivity extends AppCompatActivity {
    TextInputEditText etName, etLastname, etEmail, etPass, etPass2;
    TextView tvLoginHere;
    Button btnRegister;
    String name, lastname, email, pass, pass2;
    Helpers helper;
    AuthenticationHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etNameReg);
        etLastname = findViewById(R.id.etLastNameReg);
        etEmail = findViewById(R.id.etEmailReg);
        etPass = findViewById(R.id.etPassReg);
        etPass2 = findViewById(R.id.etPassReg2);
        tvLoginHere = findViewById(R.id.tvLoginAqui);
        btnRegister = findViewById(R.id.btnRegister);

        helper = new Helpers();
        authHelper = new AuthenticationHelper();

        btnRegister.setOnClickListener( view -> {
            createUser();
        });
        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class ));
        });
    }

    private void createUser(){
        name = etName.getText().toString().trim();
        lastname = etLastname.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        pass = etPass.getText().toString();
        pass2 = etPass2.getText().toString();
        if(validateData()) {
            authHelper.CreateUserAuth(this, email, helper.capitalizeFirstLetter(name),helper.capitalizeFirstLetter(lastname), pass);
        }
    }

    private boolean validateData(){
        boolean valid = true;
        // Validar nombre
        if(TextUtils.isEmpty(name)){
            etName.setError("El nombre no puede ir vacío");
            etName.requestFocus();
            valid = false;
        }
        // Validar apellido
        if(TextUtils.isEmpty(lastname)){
            etEmail.setError("El apellido no puede ir vacío");
            etEmail.requestFocus();
            valid = false;
        }

        // Validar email
        if(TextUtils.isEmpty(email)){
            etEmail.setError("El email no puede ir vacío");
            etEmail.requestFocus();
            valid = false;
        } else if (helper.isValidEmail(email)) {
            etEmail.setError("El email no es valido");
            etEmail.requestFocus();
            valid = false;
        }

        // Validar pass
        if(pass.length()<5){
            etPass.setError("La contraseña debe tener 6 o más caracteres");
            etPass.requestFocus();
            valid = false;
        }else if(!TextUtils.equals(pass, pass2)){
            etPass2.setError("Las contraseñas deben coincidir");
            etPass2.requestFocus();
            valid = false;
        }

        return valid;
    }
}