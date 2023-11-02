package com.example.quemefaltaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JoinHomeActivity extends AppCompatActivity implements OnResultListener {
    EditText etCodeHome;
    Button btnJoinHome;
    TextView tvBack;
    LocalStorageHelper lsHelper;
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

        user = lsHelper.getLocalUser(this);
        userId = lsHelper.getLocalId(this);

        tvBack.setOnClickListener( view -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });

        btnJoinHome.setOnClickListener( view -> {
            String codeHome = etCodeHome.getText().toString();
            if(validateCode(codeHome)){
                JoinHome(codeHome);
            }
        });
    }

    public void JoinHome(String codeHome){

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