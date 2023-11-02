package com.example.quemefaltaapp;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationHelper {
    private FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;

    public void CreateUserAuth(Context context, String email, String name, String lastname, String pass){
        mAuth = FirebaseAuth.getInstance();
        dbHelper = new DatabaseHelper();
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                boolean isSuccess = task.isSuccessful();
                if (isSuccess){
                    User user = new User(email, name, lastname);
                    dbHelper.createUserDB(context, user, task.getResult().getUser().getUid());
                } else{
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        Toast.makeText(context, "El correo ingresado no es valido o se encuentra en uso", Toast.LENGTH_LONG).show();
                    } catch (Exception e){
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void LoginUserAuth(String email, String pass, OnResultListener listener){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    listener.onResultSuccess();
                } else{
                    try{
                        throw task.getException();
                    } catch (Exception e){
                        listener.onResultFailure("Correo o contraseña inválido");
                    }
                }
            }
        });
    }

    public void Logout(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }

    public FirebaseUser getUser(){
        mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser();
    }

    public void ResetPassAuth(Context context, String email){
        mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Revisa tu correo para resetear tu password", Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context, LoginActivity.class ));
                }
            }
        });
    }

    public void VerifyLog(Context context){
        mAuth = FirebaseAuth.getInstance();

    }

}
