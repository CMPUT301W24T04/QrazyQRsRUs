package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainMenu extends Fragment {
    /**
     * Allow the user to select who we are viewing the app as for debugging
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
        View MainMenuLayout =  inflater.inflate(R.layout.fragment_main_menu, container, false);

        // navigate to new view based on who we click
        MainMenuLayout.findViewById(R.id.textView_organizer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(MainMenuLayout).navigate(R.id.action_mainMenu_to_attendeeList);
            }
        });


//         SAVE FOR LATER
        MainMenuLayout.findViewById(R.id.textView_attendee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(MainMenuLayout).navigate(R.id.action_mainMenu_to_eventList);
            }
        });


//
//        MainMenuLayout.findViewById(R.id.textView_admin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        // Inflate the layout for this fragment
        return MainMenuLayout;
    }
}