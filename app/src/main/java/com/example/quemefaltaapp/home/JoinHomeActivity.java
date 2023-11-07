package com.example.quemefaltaapp.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quemefaltaapp.MainActivity;
import com.example.quemefaltaapp.R;
import com.example.quemefaltaapp.classes.SessionManager;
import com.example.quemefaltaapp.classes.User;
import com.example.quemefaltaapp.helpers.DatabaseHelper;
import com.example.quemefaltaapp.helpers.LocalStorageHelper;
import com.example.quemefaltaapp.interfaces.OnDataResultListener;
import com.example.quemefaltaapp.interfaces.OnDocumentResultListener;
import com.example.quemefaltaapp.interfaces.OnResultListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class JoinHomeActivity extends AppCompatActivity implements OnResultListener {
    EditText etCodeHome;
    Button btnJoinHome;
    TextView tvBack;
    LocalStorageHelper lsHelper;
    DatabaseHelper dbHelper;
    SessionManager sessionManager;
    User user;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_home);
        etCodeHome = findViewById(R.id.etHomeNameJoin);
        btnJoinHome = findViewById(R.id.btnHomeJoin);
        tvBack = findViewById(R.id.tvBackHomeJoin);

        lsHelper = new LocalStorageHelper();
        dbHelper = new DatabaseHelper();
        sessionManager = SessionManager.getInstance(this);

        user = lsHelper.getLocalUser(this);
        userId = sessionManager.getUserId();

        tvBack.setOnClickListener( view -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });

        btnJoinHome.setOnClickListener( view -> {
            String codeHome = etCodeHome.getText().toString();
            if(validateCode(codeHome)){
                validateHomeDB(codeHome);
            }
        });
    }

    private void validateHomeDB(String code){
        dbHelper.getHomeByKey(code, new OnDocumentResultListener() {
            @Override
            public void onDocumentRetrieved(DocumentSnapshot documentHome) {
                List<String> updatedHomes = user.getHomes();
                updatedHomes.add(documentHome.getId());
                user.setHomes(updatedHomes);
                dbHelper.updateUser(user, userId, new OnResultListener() {
                    @Override
                    public void onResultSuccess() {
                        lsHelper.saveLocalUser(JoinHomeActivity.this,user);
                        Toast.makeText(JoinHomeActivity.this, "Hogar creado exitosamente", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(JoinHomeActivity.this, MainActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onResultFailure(String errorMessage) {
                        Toast.makeText(JoinHomeActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDocumentRetrievalFailure(String errorMessage) {
                Toast.makeText(JoinHomeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validateCode(String code){
        boolean valid = true;
        if(TextUtils.isEmpty(code)){
            etCodeHome.setError("El campo no puede ir vacio");
            etCodeHome.requestFocus();
            valid = false;
        }
        return valid;
    }

    @Override
    public void onResultSuccess() {

    }

    @Override
    public void onResultFailure(String errorMessage) {

    }
}