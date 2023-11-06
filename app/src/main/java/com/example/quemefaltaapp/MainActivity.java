package com.example.quemefaltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.quemefaltaapp.auth.LoginActivity;
import com.example.quemefaltaapp.classes.Home;
import com.example.quemefaltaapp.classes.SessionManager;
import com.example.quemefaltaapp.classes.User;
import com.example.quemefaltaapp.helpers.DatabaseHelper;
import com.example.quemefaltaapp.helpers.LocalStorageHelper;
import com.example.quemefaltaapp.home.FirstStepActivity;
import com.example.quemefaltaapp.interfaces.OnHomesResultListener;
import com.example.quemefaltaapp.interfaces.OnUserResultListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    String userId;
    LocalStorageHelper lsHelper;
    DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = SessionManager.getInstance(this);
        lsHelper = new LocalStorageHelper();
        dbHelper = new DatabaseHelper();
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(!sessionManager.isLoggedIn()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        userId = sessionManager.getUserId();
        dbHelper.getUser(userId, new OnUserResultListener() {
            @Override
            public void onUserRetrieved(User user) {
                if(user.getHomes().size() == 0){
                    startActivity(new Intent(MainActivity.this, FirstStepActivity.class));
                } else {
                    dbHelper.GetHomes(user.getHomes(), new OnHomesResultListener() {
                        @Override
                        public void onHomesRetrieved(List<Home> homes) {
                            lsHelper.saveLocalUser(MainActivity.this, user, userId);
                            lsHelper.saveHomeList(MainActivity.this, homes);
                            startActivity(new Intent(MainActivity.this, ProductsListActivity.class));
                        }

                        @Override
                        public void onHomesRetrievalFailure(String errorMessage) {
                            Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
            @Override
            public void onUserRetrievalFailure(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}