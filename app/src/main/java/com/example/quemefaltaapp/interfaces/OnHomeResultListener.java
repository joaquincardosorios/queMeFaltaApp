package com.example.quemefaltaapp.interfaces;

import com.example.quemefaltaapp.classes.Home;

public interface OnHomeResultListener {
    void onHomeRetrieved(Home home);
    void onHomeRetrievalFailure(String errorMessage);
}
