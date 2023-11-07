package com.example.quemefaltaapp.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quemefaltaapp.MainActivity;
import com.example.quemefaltaapp.ProductsListActivity;
import com.example.quemefaltaapp.R;
import com.example.quemefaltaapp.auth.LoginActivity;
import com.example.quemefaltaapp.classes.Home;
import com.example.quemefaltaapp.classes.SessionManager;
import com.example.quemefaltaapp.classes.User;
import com.example.quemefaltaapp.helpers.DatabaseHelper;
import com.example.quemefaltaapp.helpers.LocalStorageHelper;
import com.example.quemefaltaapp.home.CreateHomeActivity;
import com.example.quemefaltaapp.home.FirstStepActivity;
import com.example.quemefaltaapp.home.JoinHomeActivity;
import com.example.quemefaltaapp.interfaces.OnDocumentResultListener;
import com.example.quemefaltaapp.interfaces.OnDocumentsResultListener;
import com.example.quemefaltaapp.interfaces.OnHomesResultListener;
import com.example.quemefaltaapp.interfaces.OnUserResultListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HomesSettingFragment extends Fragment {
    ListView lvYourHomes, lvHomes;
    ImageButton btnBack;
    Button btnCreateHome, btnJoinHome;
    User user;
    String userId;
    List<Home> homes;
    LocalStorageHelper lsHelper;
    SessionManager sessionManager;

    DatabaseHelper dbHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homes_setting, container, false);
        lvHomes = view.findViewById(R.id.lvHomesSettings);
        lvYourHomes = view.findViewById(R.id.lvYourHomesSettings);
        btnCreateHome = view.findViewById(R.id.btnCreateHomeSettings);
        btnJoinHome = view.findViewById(R.id.btnKoinHomeSettings);

        lsHelper = new LocalStorageHelper();
        dbHelper = new DatabaseHelper();
        sessionManager = SessionManager.getInstance(getContext());
        userId = sessionManager.getUserId();
        dbHelper.getUser(userId, new OnDocumentResultListener() {
            @Override
            public void onDocumentRetrieved(DocumentSnapshot documentUser) {
                user = documentUser.toObject(User.class);
                lsHelper.saveLocalUser(getContext(), user);
                if(user.getHomes().size() == 0){
                    startActivity(new Intent(getContext(), FirstStepActivity.class));
                } else {
                    dbHelper.GetHomes(user.getHomes(), new OnDocumentsResultListener() {
                        @Override
                        public void onDocumentsRetrieved(List<DocumentSnapshot> documentsHomes) {
                            ArrayAdapter<String> adapterYourHomes = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
                            ArrayAdapter<String> adapterHomes = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
                            List<Home> yourHomesList = new ArrayList<>();
                            List<Home> homesList = new ArrayList<>();
                            homes = new ArrayList<>();
                            for(DocumentSnapshot documentHome : documentsHomes){
                                if(documentHome.get("creator").toString().equals(userId)){
                                    adapterYourHomes.add(documentHome.get("name").toString());
                                } else {
                                    adapterHomes.add(documentHome.get("name").toString());
                                }
                                homes.add(documentHome.toObject(Home.class));
                            }
                            lsHelper.saveHomeList(getContext(), homes);

                            lvHomes.setAdapter(adapterHomes);
                            lvYourHomes.setAdapter(adapterYourHomes);
                        }
                        @Override
                        public void onDocumentsRetrievalFailure(String errorMessage) {
                            Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                    });

                }
            }
            @Override
            public void onDocumentRetrievalFailure(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        btnCreateHome.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CreateHomeActivity.class));
        });

        btnJoinHome.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), JoinHomeActivity.class));
        });



        return view;
    }
}