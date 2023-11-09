package com.example.quemefaltaapp.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quemefaltaapp.R;
import com.example.quemefaltaapp.auth.LoginActivity;
import com.example.quemefaltaapp.classes.SessionManager;
import com.example.quemefaltaapp.classes.User;
import com.example.quemefaltaapp.helpers.AuthenticationHelper;
import com.example.quemefaltaapp.helpers.DatabaseHelper;
import com.example.quemefaltaapp.helpers.Helpers;
import com.example.quemefaltaapp.helpers.LocalStorageHelper;
import com.example.quemefaltaapp.interfaces.OnDocumentResultListener;
import com.example.quemefaltaapp.interfaces.OnResultListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserSettingFragment extends Fragment {
    TextInputLayout tlPass2;
    EditText etName, etLastname, etEmail, etPass, etPass2;
    Button btnBack, btnUpdate, btnEliminate;
    Helpers helper;
    LocalStorageHelper lsHelper;
    AuthenticationHelper authHelper;
    DatabaseHelper dbHelper;
    SessionManager sessionManager;
    String name, lastname, email, pass, pass2;
    User user;
    String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_setting, container, false);
        etName = view.findViewById(R.id.etNameUpd);
        etLastname = view.findViewById(R.id.etLastNameUpd);
        etEmail = view.findViewById(R.id.etEmailUpd);
        etPass = view.findViewById(R.id.etPassUpd);
        etPass2 = view.findViewById(R.id.etPassUpd2);
        tlPass2 = view.findViewById(R.id.tlPass2);
        btnUpdate = view.findViewById(R.id.btnUpdateUser);
        btnEliminate = view.findViewById(R.id.btnDeleteUser);


        helper = new Helpers();
        lsHelper = new LocalStorageHelper();
        sessionManager = SessionManager.getInstance(getContext());
        authHelper = new AuthenticationHelper();
        dbHelper = new DatabaseHelper();

        user = lsHelper.getLocalUser(getContext());
        userId = sessionManager.getUserId();
        fillUserData(user.getName(), user.getLastname(), user.getEmail());

        etPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    etPass2.setText("");
                    tlPass2.setEnabled(false);
                    etPass2.setEnabled(false);
                } else {
                    tlPass2.setEnabled(true);
                    etPass2.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnUpdate.setOnClickListener(v -> {
            if(validateData()){
                updateUserAuth();
            }
        });
        btnEliminate.setOnClickListener(v -> {
            confirmDeleteUser();
        });

        return view;
    }

    // Actualiza la información desde la base de datos
    public void onStart() {
        super.onStart();
        dbHelper.getDocument("users", userId, new OnDocumentResultListener() {
            @Override
            public void onDocumentRetrieved(DocumentSnapshot document) {
                User user = document.toObject(User.class);
                fillUserData(user.getName(), user.getLastname(), user.getEmail());
            }

            @Override
            public void onDocumentRetrievalFailure(String errorMessage) {
                Toast.makeText(getContext(), "Error: "+ errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserAuth(){
        authHelper.UpdatePassword(pass, new OnResultListener() {
            @Override
            public void onResultSuccess() {
                user.setName(etName.getText().toString());
                user.setLastname(etLastname.getText().toString());
                dbHelper.updateUser(user, userId, new OnResultListener() {
                    @Override
                    public void onResultSuccess() {
                        lsHelper.saveLocalUser(getContext(),user);
                        Toast.makeText(getContext(), "Datos actualizados con exito", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResultFailure(String errorMessage) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onResultFailure(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fillUserData(String name, String lastname, String email){
        etName.setText(name);
        etLastname.setText(lastname);
        etEmail.setText(email);
    }

    private boolean validateData(){
        name = etName.getText().toString();
        lastname = etLastname.getText().toString();
        email = etEmail.getText().toString();
        pass = etPass.getText().toString();
        pass2 = etPass2.getText().toString();
        boolean valid = true;
        if(TextUtils.isEmpty(name)){
            etName.setError("Nombre no puede ir vacio");
            etName.requestFocus();
            valid = false;
        }
        if(TextUtils.isEmpty(lastname)){
            etLastname.setError("Apellido no puede ir vacio");
            etLastname.requestFocus();
            valid = false;
        }
        if(TextUtils.isEmpty(email)){
            etEmail.setError("Email no puede ir vacio");
            etEmail.requestFocus();
            valid = false;
        } else if(!helper.isValidEmail(email)){
            etEmail.setError("Formato de email no es válido");
            etEmail.requestFocus();
            valid = false;
        }
        if(pass.length() != 0 && pass.length()<6){
            etPass.setError("Contraseña debe ser mayor o igual a 6 caracteres");
            etPass.requestFocus();
            valid = false;
        } else if (pass.length() != 0 && !pass.equals(pass2)) {
            etPass2.setError("Las contraseñas deben ser iguales");
            etPass2.requestFocus();
            valid = false;
        }
        return valid;
    }

    private void confirmDeleteUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de que deseas eliminar este usuario?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void deleteUser(){
        dbHelper.DeleteDocument("users", userId, new OnResultListener() {
            @Override
            public void onResultSuccess() {
                authHelper.DeleteUser(new OnResultListener() {
                    @Override
                    public void onResultSuccess() {
                        sessionManager.logoutUser();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                    @Override
                    public void onResultFailure(String errorMessage) {
                        Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResultFailure(String errorMessage) {
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}