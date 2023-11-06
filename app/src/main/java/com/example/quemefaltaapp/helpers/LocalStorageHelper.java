package com.example.quemefaltaapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.quemefaltaapp.auth.LoginActivity;
import com.example.quemefaltaapp.classes.User;
import com.google.gson.Gson;

public class LocalStorageHelper {
    public User getLocalUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", null);
        if (userJson == null) {
            Toast.makeText(context, "Usuario no encontrado, inicie sesión", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class));
        }
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        return user;
    }

    public String getLocalId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        if (id == null) {
            Toast.makeText(context, "Usuario no encontrado, inicie sesión", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class));
        }
        return id;
    }

    public void saveLocalUser(Context context, User user, String uid){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString("user", userJson);
        editor.putString("id", uid);
        editor.apply();
    }
}
