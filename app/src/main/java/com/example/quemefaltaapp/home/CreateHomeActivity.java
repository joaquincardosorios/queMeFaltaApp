package com.example.quemefaltaapp.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quemefaltaapp.MainActivity;
import com.example.quemefaltaapp.R;
import com.example.quemefaltaapp.classes.Home;
import com.example.quemefaltaapp.classes.SessionManager;
import com.example.quemefaltaapp.classes.User;
import com.example.quemefaltaapp.helpers.DatabaseHelper;
import com.example.quemefaltaapp.helpers.Helpers;
import com.example.quemefaltaapp.helpers.LocalStorageHelper;
import com.example.quemefaltaapp.interfaces.OnDataResultListener;
import com.example.quemefaltaapp.interfaces.OnResultListener;

import java.util.ArrayList;
import java.util.List;

public class CreateHomeActivity extends AppCompatActivity {

    EditText etName, etCode;
    Button btnCreate;
    ImageButton btnCopy;
    TextView tvBack;
    User user;
    Helpers helper;
    DatabaseHelper dbHelper;
    LocalStorageHelper lsHelper;
    SessionManager sessionManager;
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
        sessionManager = SessionManager.getInstance(this);

        user = lsHelper.getLocalUser(this);
        userId = sessionManager.getUserId();

        if(user.getHomes().size() != 0){
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
            List<String> categories = new ArrayList<>();
            categories.add("el refrigerador");
            categories.add("la despensa");
            categories.add("el baño");
            Home home = new Home(code, name, categories);
            dbHelper.createHome(home, userId, new OnDataResultListener() {
                @Override
                public void onDataRetrieved(String homeId) {
                    // Lista con nuevos hogares
                    List<String> homes = user.getHomes();
                    // Actualiza instancia user
                    homes.add(homeId);
                    user.setHomes(homes);
                    // Actualiza homes
                    dbHelper.updateUser(user, userId, new OnResultListener() {
                        @Override
                        public void onResultSuccess() {
                            lsHelper.saveLocalUser(CreateHomeActivity.this, user, userId);
                            etName.setEnabled(false);
                            etCode.setVisibility(View.VISIBLE);
                            btnCopy.setVisibility(View.VISIBLE);
                            etCode.setText(code);
                            etCode.setEnabled(false);
                            btnCreate.setEnabled(false);
                            Toast.makeText(CreateHomeActivity.this, "Hogar creado exitosamente, copie el codigo y envialo a tu grupo", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onResultFailure(String errorMessage) {
                            Toast.makeText(CreateHomeActivity.this, "Error :" + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onDataRetrievalFailure(String errorMessage) {
                    Toast.makeText(CreateHomeActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
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

}