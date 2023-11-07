package com.example.quemefaltaapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quemefaltaapp.auth.LoginActivity;
import com.example.quemefaltaapp.helpers.AuthenticationHelper;
import com.example.quemefaltaapp.settings.HomesSettingFragment;
import com.example.quemefaltaapp.settings.UserSettingFragment;


public class SettingsFragment extends Fragment {
    ListView lv_settings;
    AuthenticationHelper authHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        authHelper = new AuthenticationHelper();
        lv_settings = view.findViewById(R.id.lv_settings);
        String[] options = {"Usuario", "Hogares","Categorias", "Opciones", "Cerrar Sesión"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, options);
        lv_settings.setAdapter(adapter);
        lv_settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = null;
                FragmentManager fragmentManager = getParentFragmentManager();
                if(position == 0){
                    fragment = new UserSettingFragment();
                } else if (position == 1) {
                    fragment = new HomesSettingFragment();
                } else if (position == 2) {
                    fragment = new HomeFragment();
                } else if (position == 3) {
                    Toast.makeText(getActivity(), "Proximamente", Toast.LENGTH_SHORT).show();
                } else if (position == 4) {
                    authHelper.Logout();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }

                if (fragment != null) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_layout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        return view;

    }

}