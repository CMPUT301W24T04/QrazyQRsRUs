package com.example.qrazyqrsrus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.qrazyqrsrus.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ChangeFragment(new HomeFragment());


        binding.BottomNavView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home){
                ChangeFragment(new HomeFragment());
            } else if (id == R.id.scan) {
                // TO DO
            } else if (id == R.id.my_events) {
                ChangeFragment(new MyEventsFragment());
            } else if (id == R.id.profile) {
                ChangeFragment(new ProfileFragment());
            }

            return true;
        });

    }

    private void ChangeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}