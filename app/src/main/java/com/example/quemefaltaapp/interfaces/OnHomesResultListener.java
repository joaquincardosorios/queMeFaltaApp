package com.example.quemefaltaapp.interfaces;

import com.example.quemefaltaapp.classes.Home;

import java.util.List;

public interface OnHomesResultListener {
    void onHomesRetrieved(List<Home> homes);
    void onHomesRetrievalFailure(String errorMessage);
}
