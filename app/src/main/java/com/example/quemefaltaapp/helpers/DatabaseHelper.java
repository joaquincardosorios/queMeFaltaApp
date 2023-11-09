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

    public DatabaseHelper(){
        DbRef = FirebaseFirestore.getInstance();
    }

    public void createUserDB(Context context, User user, String id){
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

    public void getDocument(String collection, String id, OnDocumentResultListener listener){
        DocumentReference docRef = DbRef.collection(collection).document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        listener.onDocumentRetrieved(document);
                    } else {
                        listener.onDocumentRetrievalFailure("Elemento no encontrado");
                    }
                } else {
                    listener.onDocumentRetrievalFailure("Hubo un error");
                }
            }
        });
    }

    public void DeleteDocument(String collection, String id, OnResultListener listener){
        DocumentReference docRef = DbRef.collection(collection).document(id);
        docRef.delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
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

    public void updateUser(User user, String id, OnResultListener listener){
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
    public void UpdateDocument(String collection, String id, Object document, OnResultListener listener){
        DbRef
                .collection(collection)
                .document(id)
                .set(document)
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
