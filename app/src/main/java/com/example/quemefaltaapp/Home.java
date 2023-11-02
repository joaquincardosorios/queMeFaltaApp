package com.example.quemefaltaapp;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class Home {
    String HomeCode;
    DocumentReference Creator;
    String Name;
    List<String> Categories;
    //List<Product> products;


    public Home() {
    }

    public Home(String homeCode, String name, List<String> categories) {
        HomeCode = homeCode;
        Name = name;
        Categories = categories;
    }

    public Home(String homeCode, DocumentReference creator, String name, List<String> categories) {
        HomeCode = homeCode;
        Creator = creator;
        Name = name;
        Categories = categories;
    }

    public String getHomeCode() {
        return HomeCode;
    }

    public void setHomeCode(String homeCode) {
        HomeCode = homeCode;
    }

    public DocumentReference getCreator() {
        return Creator;
    }

    public void setCreator(DocumentReference creator) {
        Creator = creator;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<String> getCategories() {
        return Categories;
    }

    public void setCategories(List<String> categories) {
        Categories = categories;
    }
}
