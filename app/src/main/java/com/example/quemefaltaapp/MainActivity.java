package com.example.quemefaltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.quemefaltaapp.classes.User;
import com.example.quemefaltaapp.helpers.LocalStorageHelper;
import com.example.quemefaltaapp.home.FirstStepActivity;

public class MainActivity extends AppCompatActivity {

    User user;
    LocalStorageHelper lsHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lsHelper = new LocalStorageHelper();
    }

    @Override
    protected void onStart(){
        super.onStart();
        user = lsHelper.getLocalUser(this);
        Toast.makeText(this, user.getHomes().size() +"", Toast.LENGTH_SHORT).show();
        if(user.getHomes().size() == 0){
            startActivity(new Intent(MainActivity.this, FirstStepActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, ProductsListActivity.class));
        }
    }

}