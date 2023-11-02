package com.example.quemefaltaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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