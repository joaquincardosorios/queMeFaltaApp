package com.example.quemefaltaapp.interfaces;

import com.google.firebase.firestore.DocumentSnapshot;

public interface OnDocumentResultListener {
    void onDocumentRetrieved(DocumentSnapshot document);
    void onDocumentRetrievalFailure(String errorMessage);
}
