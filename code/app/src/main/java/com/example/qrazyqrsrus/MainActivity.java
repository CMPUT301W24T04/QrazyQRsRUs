package com.example.qrazyqrsrus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

//temporarily implements NewEventTextFragment.AdddEventListener until we get firestore functionality
public class MainActivity extends AppCompatActivity implements NewEventTextFragment.AddEventListener {
    private NavHostFragment navHostFragment;
//    private NavController navController = Navigation.findNavController(this, R.id.new_event_nav_host);
    private NavController navController;
    private ArrayList<Event> eventList = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //I DONT THINK I NEED ANY OF THIS
//        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.new_event_nav_host);
        //navHostFragment = NewEventNavHostFragment.newInstance();
        //navController = navHostFragment.getNavController();

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
}