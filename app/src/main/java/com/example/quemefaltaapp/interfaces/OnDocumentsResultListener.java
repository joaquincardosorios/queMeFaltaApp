package com.example.quemefaltaapp.interfaces;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface OnDocumentsResultListener {
    void onDocumentsRetrieved(List<DocumentSnapshot> documents);
    void onDocumentsRetrievalFailure(String errorMessage);
}
