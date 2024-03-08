package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment} factory method to
 * create an instance of this fragment.
 */

public class HomeFragment extends Fragment {
    ListView checkedIn;
    ListView signedUp;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment

        if (getArguments() == null){
            System.out.println("No Arguments provided");
            return null;
        }
        ListView checkedIn = view.findViewById(R.id.checked_in_events_listview);
        ListView signedUp = view.findViewById(R.id.signed_up_events_listview);

        Attendee attendee = (Attendee) getArguments().get("attendee");
        ArrayList<Event> checkedInEvents = new ArrayList<>();
        ArrayList<Event> signedUpEvents = new ArrayList<>();

        FirebaseDB.getAttendeeCheckedInEvents(attendee, checkedInEvents);
        FirebaseDB.getAttendeeSignedUpEvents(attendee, signedUpEvents);

        HomeCheckedInListAdapter homeCheckedInListAdapter = new HomeCheckedInListAdapter(getContext(), checkedInEvents);
        HomeSignedUpListAdapter homeSignedUpListAdapter = new HomeSignedUpListAdapter(getContext(), signedUpEvents);
        checkedIn.setAdapter(homeCheckedInListAdapter);
        signedUp.setAdapter(homeSignedUpListAdapter);


        return view;
    }

    public static HomeFragment newInstance(Attendee attendee){
        Bundle args = new Bundle();
        args.putSerializable("attendee", attendee);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}