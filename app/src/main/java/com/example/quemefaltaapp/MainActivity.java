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
import com.example.quemefaltaapp.interfaces.OnDocumentResultListener;
import com.example.quemefaltaapp.interfaces.OnDocumentsResultListener;
import com.example.quemefaltaapp.interfaces.OnHomesResultListener;
import com.example.quemefaltaapp.interfaces.OnUserResultListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
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
        dbHelper.getUser(userId, new OnDocumentResultListener() {
            @Override
            public void onDocumentRetrieved(DocumentSnapshot document) {
                User user = document.toObject(User.class);
                lsHelper.saveLocalUser(MainActivity.this, user);
                if(user.getHomes().size() == 0){
                    startActivity(new Intent(MainActivity.this, FirstStepActivity.class));
                } else {
                    dbHelper.GetHomes(user.getHomes(), new OnDocumentsResultListener() {
                        @Override
                        public void onDocumentsRetrieved(List<DocumentSnapshot> documents) {
                            List<Home> homes = new ArrayList<>();
                            for(DocumentSnapshot document : documents){
                                Home home = document.toObject(Home.class);
                                homes.add(home);
                            }
                            lsHelper.saveHomeList(MainActivity.this, homes);
                            startActivity(new Intent(MainActivity.this, ProductsListActivity.class));
                        }

                        @Override
                        public void onDocumentsRetrievalFailure(String errorMessage) {
                            Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    });

                }
            }

            @Override
            public void onDocumentRetrievalFailure(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

}