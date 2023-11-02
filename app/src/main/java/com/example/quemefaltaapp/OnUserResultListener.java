package com.example.quemefaltaapp;

public interface OnUserResultListener {
    void onUserRetrieved(User user);
    void onUserRetrievalFailure(String errorMessage);
}
