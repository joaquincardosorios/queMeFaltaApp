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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText etName, etEmail, etPass, etPass2;
    TextView tvLoginHere;
    Button btnRegister;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etNameReg);
        etEmail = findViewById(R.id.etEmailReg);
        etPass = findViewById(R.id.etPassReg);
        etPass2 = findViewById(R.id.etPassReg2);
        tvLoginHere = findViewById(R.id.tvLoginAqui);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener( view -> {
            createUser();
        });
        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class ));
        });
    }

    private void createUser(){
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();
        String pass2 = etPass2.getText().toString();

        if(TextUtils.isEmpty(name)){
            etName.setError("El nombre no puede  ir vacío");
            etName.requestFocus();
        }
        else if(TextUtils.isEmpty(email)){
            etEmail.setError("El nombre no puede  ir vacío");
            etEmail.requestFocus();
        }
        else if(pass.length()<5){
            etPass.setError("La contraseña debe tener 6 o más caracteres");
            etPass.requestFocus();
        }
        else if(!TextUtils.equals(pass, pass2)){
            etPass2.setError("Las contraseñas deben coincidir");
            etPass2.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Usuario registrado exitosamente" + name, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }
                            }
                        });
                    } else{
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}