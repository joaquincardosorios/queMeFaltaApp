package com.example.quemefaltaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AuthenticationHelper authHelper;
    private FirebaseFirestore userDbRef;
    User user;
    private LiveData<User> liveUserData = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authHelper = new AuthenticationHelper();

    }

    @Override
    protected void onStart(){
        super.onStart();
        String uid;
        userDbRef = FirebaseFirestore.getInstance();
        FirebaseUser userAuth = authHelper.getUser();
        if (userAuth == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        uid = userAuth.getProviderData().get(0).getUid();
        DocumentReference userRef = userDbRef.collection("users").document(uid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        String name = (String) document.getData().get("name");
                        String lastname = (String) document.getData().get("lastname");
                        String email = (String) document.getData().get("email");
                        boolean active = (boolean) document.getData().get("active");
                        List<String> homes = (List<String>) document.getData().get("home");
                        user = new User(email,name,lastname,homes,active);
                        ((MutableLiveData<User>) liveUserData).setValue(user);
                    }
                }
            }
        });
        liveUserData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.getHomes().size() == 0) {
                    startActivity(new Intent(MainActivity.this, FirstStepActivity.class));
                } else {

                }
            }
        });

    }

}