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
        // At the start we want to be at the Homescreen
        ChangeFragment(new HomeFragment());

        // When the navigation bar is clicked
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

    /**
     * Use this method to display the fragment that is passed
     * as an argument
     *
     * @param fragment The fragment we want to display.
     */
    private void ChangeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}