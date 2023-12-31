package com.example.quemefaltaapp.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.quemefaltaapp.R;
import com.example.quemefaltaapp.classes.User;
import com.example.quemefaltaapp.helpers.AuthenticationHelper;
import com.example.quemefaltaapp.helpers.DatabaseHelper;
import com.example.quemefaltaapp.helpers.Helpers;
import com.example.quemefaltaapp.interfaces.OnDataResultListener;
import com.example.quemefaltaapp.interfaces.OnResultListener;
import com.google.android.material.textfield.TextInputEditText;


public class RegisterActivity extends AppCompatActivity {
    TextInputEditText etName, etLastname, etEmail, etPass, etPass2;
    TextView tvLoginHere;
    Button btnRegister;
    String name, lastname, email, pass, pass2;
    Helpers helper;
    DatabaseHelper dbHelper;
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
        dbHelper = new DatabaseHelper();
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
            String capitalName = helper.capitalizeFirstLetter(name);
            String capitalLastname = helper.capitalizeFirstLetter(lastname);
            authHelper.CreateUserAuth(email, capitalName, capitalLastname, pass, new OnDataResultListener() {
                @Override
                public void onDataRetrieved(String uid) {
                    User user = new User(email, name, lastname);
                    dbHelper.createUserDB(RegisterActivity.this, user, uid);
                }

                @Override
                public void onDataRetrievalFailure(String errorMessage) {

                }
            });
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
            etLastname.setError("El apellido no puede ir vacío");
            etLastname.requestFocus();
            valid = false;
        }

        // Validar email
        if(TextUtils.isEmpty(email)){
            etEmail.setError("El email no puede ir vacío");
            etEmail.requestFocus();
            valid = false;
        } else if (!helper.isValidEmail(email)) {
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