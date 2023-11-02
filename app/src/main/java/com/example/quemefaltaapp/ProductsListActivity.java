package com.example.quemefaltaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;


import com.example.quemefaltaapp.databinding.ActivityProductsListBinding;

public class ProductsListActivity extends AppCompatActivity {
    ActivityProductsListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityProductsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ListFragment());
        binding.bottomNavigationView.getMenu().findItem(R.id.it_list).setChecked(true);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.it_home){
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.it_settings){
                replaceFragment(new SettingsFragment());
            } else {
                replaceFragment(new ListFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}