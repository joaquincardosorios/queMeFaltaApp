package com.example.quemefaltaapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DatabaseHelper {
    private FirebaseFirestore DbRef;


    public void createUserDB(Context context, User user, String id){
        DbRef = FirebaseFirestore.getInstance();
        DbRef
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

    public void getUser(String uid, OnUserResultListener listener){
        DbRef = FirebaseFirestore.getInstance();
        DocumentReference userRef = DbRef.collection("users").document(uid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()){
                    return;
                }
                DocumentSnapshot document = task.getResult();
                if(!document.exists()){
                    return;
                }
                String name = (String) document.getData().get("name");
                String lastname = (String) document.getData().get("lastname");
                String email = (String) document.getData().get("email");
                boolean active = (boolean) document.getData().get("active");
                List<String> homes = (List<String>) document.getData().get("homes");
                User user = new User(email,name,lastname,homes,active);
                listener.onUserRetrieved(user);
            }
        });
    }

    public void createHome(Home home, String idUser, OnResultListener listener){
        DbRef = FirebaseFirestore.getInstance();
        DocumentReference userRef = DbRef.collection("users").document(idUser);
        home.setCreator(userRef);
        DbRef
            .collection("homes")
            .add(home)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    listener.onResultSuccess();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onResultFailure("Hubo un error: " + e.getMessage());
                }
            });
    }


}
