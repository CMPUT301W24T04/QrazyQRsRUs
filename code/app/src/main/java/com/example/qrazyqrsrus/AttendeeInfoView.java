package com.example.qrazyqrsrus;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.auth.User;

/**
 * Shows attendee information when list is clicked
 */
public class AttendeeInfoView extends Fragment {
    static AttendeeList newInstance(Attendee attendee){
        Bundle args = new Bundle();
        args.putSerializable("attendee", attendee);
        AttendeeList fragment = new AttendeeList();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return attendeeInfoLayout
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View attendeeInfoLayout = inflater.inflate(R.layout.fragment_attendee_info_view, container, false);

        // get bundle of attendees
        //https://stackoverflow.com/questions/42266436/passing-objects-between-fragments
        Bundle bundle = getArguments();
        Attendee attendee = (Attendee) bundle.getSerializable("current_attendee");
//        Attendee attendee = (Attendee) getArguments().get("current_attendee");



        // Set textview value to correct name
        TextView attendeeName = attendeeInfoLayout.findViewById(R.id.attendee_name);
        attendeeName.setText(attendee.getName());
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