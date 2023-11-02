package com.example.quemefaltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
    String code;
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

        btnCopy.setOnClickListener(view -> {
            String code = etCode.getText().toString();
            helper.CopyText(this, code);
        });

        btnCreate.setOnClickListener(view -> {
            createHome();
        });
    }

    private void createHome(){
        String name = etName.getText().toString();
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Hogar creado exitosamente, copie el codigo y envialo a tu grupo", Toast.LENGTH_LONG).show();
        //TODO ingresar nuevo hogar a User
        etName.setEnabled(false);
        etCode.setVisibility(View.VISIBLE);
        btnCopy.setVisibility(View.VISIBLE);
        etCode.setText(code);
        etCode.setEnabled(false);
    }

    @Override
    public void onResultFailure(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}