package com.example.quemefaltaapp.classes;

import java.util.Date;

public class Product {
    String Name;
    int Priority;
    int Quantity;
    Boolean State;
    Date LastPurchaseDate;
    Date LastRequestedDate;

    public Product() {
    }

    public Product(String name, int priority, int quantity, Boolean state, Date lastPurchaseDate, Date lastRequestedDate) {
        Name = name;
        Priority = priority;
        Quantity = quantity;
        State = state;
        LastPurchaseDate = lastPurchaseDate;
        LastRequestedDate = lastRequestedDate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public Boolean getState() {
        return State;
    }

    public void setState(Boolean state) {
        State = state;
    }

    public Date getLastPurchaseDate() {
        return LastPurchaseDate;
    }

    public void setLastPurchaseDate(Date lastPurchaseDate) {
        LastPurchaseDate = lastPurchaseDate;
    }

    public Date getLastRequestedDate() {
        return LastRequestedDate;
    }

    public void setLastRequestedDate(Date lastRequestedDate) {
        LastRequestedDate = lastRequestedDate;
    }
}
