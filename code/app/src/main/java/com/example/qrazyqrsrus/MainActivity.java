package com.example.qrazyqrsrus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.qrazyqrsrus.databinding.ActivityMainBinding;


import java.util.ArrayList;

//temporarily implements NewEventTextFragment.AdddEventListener until we get firestore functionality
public class MainActivity extends AppCompatActivity implements NewEventTextFragment.AddEventListener {
    private ActivityMainBinding binding;

    private FirebaseFirestore db;
    private NavHostFragment navHostFragment;
//    private NavController navController = Navigation.findNavController(this, R.id.new_event_nav_host);
    private NavController navController;
    private ArrayList<Event> eventList = new ArrayList<Event>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        final CollectionReference usersCollection = db.collection("Users");
        final CollectionReference eventsCollection = db.collection("Events");
        // At the start we want to be at the Home screen
        ChangeFragment(new HomeFragment());

        // When the navigation bar is clicked
        binding.BottomNavView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();


            if (id == R.id.home) {
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
    //I DONT THINK I NEED ANY OF THIS
//    @Override
//    public void setNavController() {
//
//        //navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.new_event_nav_host);
//        //navController = navHostFragment.getNavController();
//    }
    //temporarily have an addEvent method. should eventuall be changed to be an observer and update when model (firestore) is changed.
    public void addEvent(Event event){
        eventList.add(event);
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