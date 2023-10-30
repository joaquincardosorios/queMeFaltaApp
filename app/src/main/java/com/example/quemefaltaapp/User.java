package com.example.quemefaltaapp;

import java.util.ArrayList;
import java.util.List;

public class User {
    String Email;
    String Name;
    String Lastname;
    List<String> homes;
    boolean active;

    public User(String email, String name, String lastname, List<String> homes, boolean active) {
        this.Email = email;
        this.Name = name;
        this.Lastname = lastname;
        this.homes = (homes != null) ? homes : new ArrayList<>();;
        this.active = active;
    }
    public User(String email,String name, String lastname) {
        this.Email = email;
        this.Name = name;
        this.Lastname = lastname;
        this.homes = new ArrayList<String>();
        this.active = true;
    }
    public User(){}

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public List<String> getHomes() {
        return homes;
    }

    public void setHomes(List<String> homes) {
        this.homes = homes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
