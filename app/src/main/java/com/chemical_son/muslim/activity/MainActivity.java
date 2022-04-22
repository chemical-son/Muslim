package com.chemical_son.muslim.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.chemical_son.muslim.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    Fragment homeFragment,
            quranFragment,
            listenFragment;

    BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        homeFragment = new HomeFragment();
        quranFragment = new QuranFragment();
        listenFragment = new ListenQuranFragment();

        bottomNavigationView.setSelectedItemId(R.id.quran_bottom_nav);

        setFragmentContent(quranFragment);

        bottomNavigationView.setOnItemSelectedListener(v -> {
            switch (v.getItemId()) {
                case R.id.home_bottom_nav:
                    setFragmentContent(homeFragment);
                    break;
                case R.id.quran_bottom_nav:
                    setFragmentContent(quranFragment);
                    break;
                case R.id.listen_quran_bottom_nav:
                    setFragmentContent(listenFragment);
            }
            return true;
        });

        bottomNavigationView.setOnItemReselectedListener(v -> {
            switch (v.getItemId()) {
                case R.id.home_bottom_nav:
                    setFragmentContent(homeFragment);
                    break;
                case R.id.quran_bottom_nav:
                    setFragmentContent(quranFragment);
                    break;
                case R.id.listen_quran_bottom_nav:
                    setFragmentContent(listenFragment);
            }
        });
    }

    private void setFragmentContent(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_main_activity, fragment).commit();
    }
}