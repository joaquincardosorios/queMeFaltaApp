package com.example.quemefaltaapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quemefaltaapp.classes.Home;
import com.example.quemefaltaapp.auth.LoginActivity;
import com.example.quemefaltaapp.interfaces.OnDataResultListener;
import com.example.quemefaltaapp.interfaces.OnDocumentResultListener;
import com.example.quemefaltaapp.interfaces.OnDocumentsResultListener;
import com.example.quemefaltaapp.interfaces.OnHomeResultListener;
import com.example.quemefaltaapp.interfaces.OnHomesResultListener;
import com.example.quemefaltaapp.interfaces.OnResultListener;
import com.example.quemefaltaapp.interfaces.OnUserResultListener;
import com.example.quemefaltaapp.classes.User;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

    public void getUser(String uid, OnDocumentResultListener listener){
        DbRef = FirebaseFirestore.getInstance();
        DocumentReference userRef = DbRef.collection("users").document(uid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        listener.onDocumentRetrieved(document);
                    } else {
                        listener.onDocumentRetrievalFailure("No se encontró usuario");
                    }
                } else {
                    listener.onDocumentRetrievalFailure("Holo");
                }
            }
        });
    }

    public void updateUser(User user, String id, OnResultListener listener){
        DbRef = FirebaseFirestore.getInstance();
        DbRef
            .collection("users")
            .document(id)
            .set(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
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

    public void createHome(Home home, String idUser, OnDataResultListener listener){
        DbRef = FirebaseFirestore.getInstance();
        home.setCreator(idUser);
        DbRef
            .collection("homes")
            .add(home)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String idHome = documentReference.getId();
                    listener.onDataRetrieved(idHome);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onDataRetrievalFailure("Error: " + e.getMessage() );
                }
            });
    }

    public  void getHomeByKey(String key, OnDocumentResultListener listener){
        DbRef = FirebaseFirestore.getInstance();
        DbRef
            .collection("homes")
            .whereEqualTo("homeCode", key)
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        listener.onDocumentRetrieved(documentSnapshot);
                    } else {
                        listener.onDocumentRetrievalFailure("No se encontró código");
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onDocumentRetrievalFailure(e.getMessage());
                }
            });
    }

    public void GetHomes(List<String> homesIdList, OnDocumentsResultListener listener){
        DbRef = FirebaseFirestore.getInstance();
        CollectionReference homeCollection = DbRef.collection("homes");
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for(String homeId : homesIdList ){
            DocumentReference homeRef = homeCollection.document(homeId);
            tasks.add(homeRef.get());
        }
        Task<List<DocumentSnapshot>> allTasks = Tasks.whenAllSuccess(tasks);
        allTasks
            .addOnSuccessListener(documentSnapshots -> {
                listener.onDocumentsRetrieved(documentSnapshots);
            })
            .addOnFailureListener( e -> {
                listener.onDocumentsRetrievalFailure(e.getMessage());
            });
    }
}
