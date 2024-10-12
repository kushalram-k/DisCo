package com.example.myapplication;
import com.example.myapplication.R;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.ActivityMainPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class mainPage extends AppCompatActivity {
    @NonNull ActivityMainPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the correct layout (activity_main_page.xml)
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());

        // Set the content view to the root of the binding
        setContentView(binding.getRoot());

        // Now you can reference bottomNavigationView correctly
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        replaceFragment(new groupFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.group) {
                replaceFragment(new groupFragment());
            } else if (itemId == R.id.person) {
                replaceFragment(new personFragment());
            } else if (itemId == R.id.settings) {
                replaceFragment(new settingsFragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}