package com.example.quemefaltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FirstStepActivity extends AppCompatActivity {
    TextView tv_logout;
    Button btn_createHome, btn_joinHome;
    AuthenticationHelper authHelper;
    LocalStorageHelper lsHelper;

    User user;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_step);
        tv_logout = findViewById(R.id.tvLogout);
        btn_createHome = findViewById(R.id.btnCrearHogar);
        btn_joinHome = findViewById(R.id.btnUnirseHogar);

        authHelper = new AuthenticationHelper();
        lsHelper = new LocalStorageHelper();

        User user = lsHelper.getLocalUser(this);

        btn_createHome.setOnClickListener(view -> {
            Intent i = new Intent(FirstStepActivity.this, CreateHomeActivity.class);
            startActivity(i);
        });
        btn_joinHome.setOnClickListener(view -> {
            startActivity(new Intent(FirstStepActivity.this, LoginActivity.class));
        });

        tv_logout.setOnClickListener(view -> {
            authHelper.Logout();
            startActivity(new Intent(FirstStepActivity.this, LoginActivity.class));
        });
    }
}