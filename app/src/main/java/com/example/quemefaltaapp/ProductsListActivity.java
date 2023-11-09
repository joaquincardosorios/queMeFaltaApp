package com.example.quemefaltaapp;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

                if (currentFragment instanceof ListFragment) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductsListActivity.this);
                    builder.setTitle("¿Cerrar la aplicación?");
                    builder.setMessage("¿Estás seguro de que quieres cerrar la aplicación?");

                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cierra la aplicación
                            finishAffinity();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cierra el diálogo y no realiza ninguna acción adicional
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                } else {
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }
            }
        });
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}