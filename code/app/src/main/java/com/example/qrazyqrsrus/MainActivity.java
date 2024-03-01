package com.example.qrazyqrsrus;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NewEventTextFragment.AddEventListener {
    private ArrayList<Event> eventList = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //temporarily have an addEvent method. should eventually be changed to be an observer and update when model (firestore) is changed.
    public void addEvent(Event event){
        eventList.add(event);
    }
}