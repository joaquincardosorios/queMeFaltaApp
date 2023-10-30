package com.example.quemefaltaapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseHelper {
    private FirebaseFirestore userDbRef;

    public void createUserDB(Context context, User user, String id){
        userDbRef = FirebaseFirestore.getInstance();
        userDbRef
            .collection("users")
            .document(id)
            .set(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Usuario registrado exitosamente", Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Hubo un error inesperado:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
    }




}
