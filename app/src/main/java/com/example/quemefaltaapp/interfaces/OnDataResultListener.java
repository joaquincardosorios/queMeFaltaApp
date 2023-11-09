package com.example.quemefaltaapp.interfaces;

public interface OnDataResultListener {
    void onDataRetrieved(String data);
    void onDataRetrievalFailure(String errorMessage);
}
