package com.example.quemefaltaapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quemefaltaapp.auth.LoginActivity;
import com.example.quemefaltaapp.classes.SessionManager;
import com.example.quemefaltaapp.interfaces.OnDataResultListener;
import com.example.quemefaltaapp.interfaces.OnResultListener;
import com.example.quemefaltaapp.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationHelper {
    private FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;

    public AuthenticationHelper() {
        mAuth = FirebaseAuth.getInstance();
        dbHelper = new DatabaseHelper();
    }

    public void CreateUserAuth(String email, String name, String lastname, String pass, OnDataResultListener listener){
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                boolean isSuccess = task.isSuccessful();
                if (isSuccess){
                    User user = new User(email, name, lastname);
                    FirebaseUser userAuth = mAuth.getCurrentUser();
                    sendVerificationEmail(userAuth, user, listener);
                } else{
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        listener.onDataRetrievalFailure("El correo ingresado no es valido o se encuentra en uso");
                    } catch (Exception e){
                        listener.onDataRetrievalFailure(e.toString());
                    }
                }
            }
        });
    }

    public void sendVerificationEmail(FirebaseUser userAuth, User user, OnDataResultListener listener){
        userAuth.sendEmailVerification()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        String uid = userAuth.getUid();
                        listener.onDataRetrieved(uid);
                    } else {
                        listener.onDataRetrievalFailure("Error al enviar el correo de verificación");
                    }
                }
            });
    }

    public void LoginUserAuth(String email, String pass, OnDataResultListener listener){
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user != null && user.isEmailVerified()){
                        String id = user.getUid();
                        String username = user.getEmail();
                        listener.onDataRetrieved(id+"#"+username);
                    } else {
                        listener.onDataRetrievalFailure("Usuario no verificado");
                    }
                } else{
                    try{
                        throw task.getException();
                    } catch (Exception e){
                        listener.onDataRetrievalFailure("Correo o contraseña inválido");
                    }
                }
            }
        });
    }

    public void Logout(){
        mAuth.signOut();
    }

    public FirebaseUser getUser(){
        return mAuth.getCurrentUser();
    }

    public void ResetPassAuth(Context context, String email){
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

    public void UpdatePassword(String pass, OnResultListener listener){
        FirebaseUser user = mAuth.getCurrentUser();
        List<Task<Void>> tasks = new ArrayList<>();
        if (!TextUtils.isEmpty(pass)){
            tasks.add(user.updatePassword(pass));
        }
        Task<List<Object>> allTasks = Tasks.whenAllSuccess(tasks);
        allTasks
            .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                @Override
                public void onSuccess(List<Object> objects) {
                    listener.onResultSuccess();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onResultFailure(e.getMessage());
                }
            });
    }

    public void DeleteUser(OnResultListener listener){
        FirebaseUser user = mAuth.getCurrentUser();
        user.delete()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        listener.onResultSuccess();
                    }
                }
            });
    }



}
