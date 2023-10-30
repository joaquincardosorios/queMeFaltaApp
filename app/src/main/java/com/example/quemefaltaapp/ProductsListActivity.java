package com.example.quemefaltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProductsListActivity extends AppCompatActivity {

    TextView tv_logout;
    User user;

    AuthenticationHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        tv_logout = findViewById(R.id.tvLogoutList);
        authHelper = new AuthenticationHelper();

        tv_logout.setOnClickListener(view -> {
            authHelper.Logout();
            startActivity(new Intent(ProductsListActivity.this, LoginActivity.class));
        });
    }
}