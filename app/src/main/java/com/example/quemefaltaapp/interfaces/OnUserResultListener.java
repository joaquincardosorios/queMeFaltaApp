package com.example.quemefaltaapp.interfaces;

import com.example.quemefaltaapp.classes.User;

public interface OnUserResultListener {
    void onUserRetrieved(User user);
    void onUserRetrievalFailure(String errorMessage);
}
