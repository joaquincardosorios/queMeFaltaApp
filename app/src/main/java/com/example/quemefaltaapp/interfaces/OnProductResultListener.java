package com.example.quemefaltaapp.interfaces;

import com.example.quemefaltaapp.classes.Product;

public interface OnProductResultListener {
    void onProductRetrieved(Product product);
    void onProductRetrievalFailure(String errorMessage);
}
