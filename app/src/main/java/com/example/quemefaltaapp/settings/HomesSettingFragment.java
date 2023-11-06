package com.example.quemefaltaapp.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.quemefaltaapp.R;
import com.example.quemefaltaapp.classes.Home;
import com.example.quemefaltaapp.classes.User;
import com.example.quemefaltaapp.helpers.LocalStorageHelper;

import java.lang.reflect.Array;
import java.util.List;

public class HomesSettingFragment extends Fragment {
    ListView lvYourHomes, lvHomes;
    ImageButton btnBack;
    Button btnCreateHome, btnJoinHome;
    User user;
    String userId;
    List<Home> homes;
    LocalStorageHelper lsHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homes_setting, container, false);
        lvHomes = view.findViewById(R.id.lvHomesSettings);

        lsHelper = new LocalStorageHelper();
        homes = lsHelper.getHomeList(getContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);

        for (Home home : homes) {
            adapter.add(home.getName());
        }
        lvHomes.setAdapter(adapter);

        return view;
    }
}