package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AttendeeInfoView extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View attendeeInfoLayout = inflater.inflate(R.layout.fragment_attendee_info_view, container, false);
        attendeeInfoLayout.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(attendeeInfoLayout).navigate(R.id.action_attendeeInfoView_to_attendeeList);
            }
        });
        // Inflate the layout for this fragment
        return attendeeInfoLayout;
    }
}