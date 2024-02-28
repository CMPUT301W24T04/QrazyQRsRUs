package com.example.crazyqrtest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class OrganizerMain extends Fragment {
    /**
     * Navigates through the organizer main menu
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get the fragment layout from fragment_organizer_main
        View organizerMainLayout = inflater.inflate(R.layout.fragment_organizer_main, container, false);
//        // get the viewAttendees button

        organizerMainLayout.findViewById(R.id.button_view_attendees).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(organizerMainLayout).navigate(R.id.action_organizerMain_to_attendeeList);
            }
        });

        organizerMainLayout.findViewById(R.id.organizer_back_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(organizerMainLayout).navigate(R.id.action_organizerMain_to_mainMenu);
            }
        });

        // Inflate the layout for this fragment
        return organizerMainLayout; //inflater.inflate(R.layout.fragment_organizer_main, container, false);
    }
}