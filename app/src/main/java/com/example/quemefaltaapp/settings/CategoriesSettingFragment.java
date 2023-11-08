package com.example.quemefaltaapp.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quemefaltaapp.R;
import com.example.quemefaltaapp.classes.AdapterSpinner;
import com.example.quemefaltaapp.classes.Home;
import com.example.quemefaltaapp.classes.SessionManager;
import com.example.quemefaltaapp.helpers.DatabaseHelper;
import com.example.quemefaltaapp.helpers.LocalStorageHelper;

import java.util.ArrayList;
import java.util.List;

public class CategoriesSettingFragment extends Fragment {
    Spinner spHomes;
    Button btnCreateCategory;
    ImageButton btnBack;
    ListView lvCategories;
    LocalStorageHelper lsHelper;
    DatabaseHelper dbHelper;

    SessionManager sessionManager;
    List<Home> homes;

    List<String> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories_setting, container, false);
        spHomes = view.findViewById(R.id.spHomesCategoriesSetting);
        btnCreateCategory = view.findViewById(R.id.btnCreateCategorieSetting);
        btnBack = view.findViewById(R.id.btnBackCategoriesSettings);
        lvCategories = view.findViewById(R.id.lvCategoriesSetting);

        lsHelper = new LocalStorageHelper();
        dbHelper = new DatabaseHelper();
        sessionManager = SessionManager.getInstance(getContext());

        homes = lsHelper.getHomeList(getContext());
        List<String> homeNames = new ArrayList<>();
        for(Home home : homes){
            homeNames.add(home.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, homeNames);
        spHomes.setAdapter(adapter);

        spHomes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Home homeSelected = homes.get(position);
                categories = homeSelected.getCategories();
                Toast.makeText(getContext(), categories.get(1), Toast.LENGTH_SHORT).show();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categories);
                lvCategories.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }
}