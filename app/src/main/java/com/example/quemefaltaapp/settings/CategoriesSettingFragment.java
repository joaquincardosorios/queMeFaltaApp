package com.example.quemefaltaapp.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quemefaltaapp.R;
import com.example.quemefaltaapp.classes.AdapterMenuCategories;
import com.example.quemefaltaapp.classes.AdapterMenuCategories.OnCategoryDeleteListener;
import com.example.quemefaltaapp.classes.AdapterSpinner;
import com.example.quemefaltaapp.classes.Home;
import com.example.quemefaltaapp.classes.SessionManager;
import com.example.quemefaltaapp.classes.User;
import com.example.quemefaltaapp.helpers.DatabaseHelper;
import com.example.quemefaltaapp.helpers.LocalStorageHelper;
import com.example.quemefaltaapp.interfaces.OnDocumentResultListener;
import com.example.quemefaltaapp.interfaces.OnDocumentsResultListener;
import com.example.quemefaltaapp.interfaces.OnResultListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoriesSettingFragment extends Fragment implements OnCategoryDeleteListener {
    Spinner spHomes;
    Button btnCreateCategory;
    ImageButton btnBack;
    ListView lvCategories;
    LocalStorageHelper lsHelper;
    DatabaseHelper dbHelper;
    User user;
    SessionManager sessionManager;
    List<Home> homes;
    Home homeSelected;
    String idHomeSelected;
    int posHomeSelected;

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
                if (user != null) {
                    List<String> homesId = user.getHomes();
                    if (position < homesId.size()) {
                        posHomeSelected = position;
                        idHomeSelected = homesId.get(position);
                        updateListCategories(position);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCreateCategory.setOnClickListener(v -> {
            dialogNewCategory();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dbHelper.getDocument("users", sessionManager.getUserId(), new OnDocumentResultListener() {
            @Override
            public void onDocumentRetrieved(DocumentSnapshot userDocument) {
                user = userDocument.toObject(User.class);
                List<String> homesId = user.getHomes();
                dbHelper.GetHomes(homesId, new OnDocumentsResultListener() {
                    @Override
                    public void onDocumentsRetrieved(List<DocumentSnapshot> homeDocuments) {
                        List<Home> homesListUpdated = new ArrayList<>();
                        for( DocumentSnapshot homeDocument : homeDocuments){
                            homesListUpdated.add(homeDocument.toObject(Home.class));
                        }
                        homes.clear();
                        homes.addAll(homesListUpdated);
                        if (spHomes.getAdapter() != null) {
                            ((ArrayAdapter<String>) spHomes.getAdapter()).notifyDataSetChanged();
                        }

                        if (!homes.isEmpty()) {
                            idHomeSelected = user.getHomes().get(0);
                            updateListCategories(0);
                        }
                    }
                    @Override
                    public void onDocumentsRetrievalFailure(String errorMessage) {
                        Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDocumentRetrievalFailure(String errorMessage) {
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryDelete(int position) {
        if (homeSelected != null){
            List<String> categoriesUpd = homeSelected.getCategories();
            categoriesUpd.remove(position);
            homeSelected.setCategories(categoriesUpd);

            dbHelper.UpdateDocument("homes", idHomeSelected, homeSelected, new OnResultListener() {
                @Override
                public void onResultSuccess() {
                    updateListCategories(posHomeSelected);
                    Toast.makeText(getContext(), "Eliminado exitoso", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResultFailure(String errorMessage) {
                    Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void dialogNewCategory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Nueva Categoría");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCategory = input.getText().toString();
                if (!TextUtils.isEmpty(newCategory)) {
                    addNewCategory(newCategory);
                } else {
                    Toast.makeText(getContext(), "Por favor, ingrese un nombre de categoría válido", Toast.LENGTH_SHORT).show();
                }
            }
        });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void addNewCategory(String newCategory) {
        homeSelected.getCategories().add(newCategory);

        dbHelper.UpdateDocument("homes", idHomeSelected, homeSelected, new OnResultListener() {
            @Override
            public void onResultSuccess() {

            }

            @Override
            public void onResultFailure(String errorMessage) {

            }
        });


        Toast.makeText(getContext(), idHomeSelected, Toast.LENGTH_SHORT).show();
    }
    public void updateListCategories(int position){
        homeSelected = homes.get(position);
        List<String> categories = homeSelected.getCategories();
        AdapterMenuCategories adapter = new AdapterMenuCategories(getContext(), R.layout.list_categories_layout,categories);
        lvCategories.setAdapter(adapter);
        adapter.setOnCategoryDeleteListener(CategoriesSettingFragment.this);
    }
}