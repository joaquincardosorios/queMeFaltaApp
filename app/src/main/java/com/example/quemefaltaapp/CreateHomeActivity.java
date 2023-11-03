package com.example.quemefaltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class CreateHomeActivity extends AppCompatActivity implements OnResultListener {

    EditText etName, etCode;
    Button btnCreate;
    ImageButton btnCopy;
    TextView tvBack;
    User user;
    Helpers helper;
    DatabaseHelper dbHelper;
    LocalStorageHelper lsHelper;
    String code, userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_home);
        etName = findViewById(R.id.etHomeNameCreate);
        etCode = findViewById(R.id.etHomeCodeCreate);
        btnCreate = findViewById(R.id.btnHomeCreate);
        btnCopy = findViewById(R.id.btnCopyCreate);
        tvBack = findViewById(R.id.tvBackCreateHome);

        etCode.setVisibility(View.GONE);
        btnCopy.setVisibility(View.GONE);

        helper = new Helpers();
        dbHelper = new DatabaseHelper();
        lsHelper = new LocalStorageHelper();

        user = lsHelper.getLocalUser(this);
        userId = lsHelper.getLocalId(this);

        if(user.getHomes().size() == 0){
            startActivity(new Intent(this, FirstStepActivity.class));
        }

        btnCopy.setOnClickListener(view -> {
            String code = etCode.getText().toString();
            helper.CopyText(this, code);
        });

        btnCreate.setOnClickListener(view -> {
            createHome();
        });

        tvBack.setOnClickListener( view -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });
    }

    private void createHome(){
        String name = etName.getText().toString();
        if (validateHome(name)){
            code = helper.generateID();
            String userId = lsHelper.getLocalId(this);
            List<String> categories = new ArrayList<>();
            categories.add("el refrigerador");
            categories.add("la despensa");
            categories.add("el baño");
            Home home = new Home(code, name, categories);
            dbHelper.createHome(home, userId, this);
        }
    }

    private boolean validateHome(String name){
        boolean valid = true;
        if (name.length() < 3) {
            etName.setError("Nombre no puede tener menos de 3 caracteres");
            etName.requestFocus();
            valid = false;
        }else if(user.getHomes().size() > 0){
            for (String home : user.getHomes()) {
                if (name.equals(home)) {
                    etName.setError("El hogar no puede ser igual a uno ya existente");
                    etName.requestFocus();
                    valid = false;
                    break;
                }
            }
            if(user.getHomes().size() > 3){
                etName.setError("No se pueden tener máximo 3 hogares a la vez");
                etName.requestFocus();
            }
        }
        return valid;
    }


    @Override
    public void onResultSuccess() {
        // TODO mejorar interface OnResultSuccess
        // Lista con nuevos hogares
        List<String> homes = user.getHomes();
        // Actualiza instancia user
        homes.add(code);
        user.setHomes(homes);
        // Actualiza homes
        //TODO updater global
        dbHelper.updateUser(this, homes, userId);

        // Guarda cambios en LS
        lsHelper.saveLocalUser(this, user, userId);
        etName.setEnabled(false);
        etCode.setVisibility(View.VISIBLE);
        btnCopy.setVisibility(View.VISIBLE);
        etCode.setText(code);
        etCode.setEnabled(false);
        btnCreate.setEnabled(false);
        Toast.makeText(this, "Hogar creado exitosamente, copie el codigo y envialo a tu grupo", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onResultFailure(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}