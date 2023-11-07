package com.example.quemefaltaapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.quemefaltaapp.auth.LoginActivity;
import com.example.quemefaltaapp.classes.Home;
import com.example.quemefaltaapp.classes.User;
import com.example.quemefaltaapp.settings.HomesSettingFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
public class LocalStorageHelper {
    private static final String PREF_NAME = "Preferences";
    private static final String KEY_HOME_LIST = "homeList";
    public User getLocalUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", null);
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        return user;
    }

    public String getLocalId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        if (id == null) {
            Toast.makeText(context, "Usuario no encontrado, inicie sesión", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class));
        }
        return id;
    }

    public void saveLocalUser(Context context, User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString("user", userJson);
        editor.apply();
    }

    public void saveHomeList(Context context, List<Home> homeList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String homeListJson = gson.toJson(homeList);

        editor.putString("homeList", homeListJson);
        editor.apply();
    }

    public List<Home> getHomeList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String homeListJson = sharedPreferences.getString("homeList", null);

        if (homeListJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(homeListJson, new TypeToken<List<Home>>() {}.getType());
        } else {
            return new ArrayList<>();
        }
    }




}
