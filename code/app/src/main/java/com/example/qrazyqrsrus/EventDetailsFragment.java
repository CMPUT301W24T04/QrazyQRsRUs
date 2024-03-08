package com.example.qrazyqrsrus;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailsFragment extends Fragment {

    private Attendee attendee;
    private Event event;
    public EventDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() == null){
            System.out.println("No Arguments provided");
            return null;
        }
        this.event = (Event) getArguments().get("event");
        this.attendee = (Attendee) getArguments().get("attendee");

        if (this.attendee == null){
            String userId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            FirebaseDB.loginUser(userId, new FirebaseDB.GetAttendeeCallBack() {
                @Override
                public void onResult(Attendee attendee) {
                    setAttendee(attendee);
                }
            });
        }
        Boolean isCheckIn = (Boolean) getArguments().get("isCheckIn");
        if (isCheckIn == null) {

        }else{
            if (isCheckIn){
                if (event.getSignUps().contains(attendee.getDocumentId())){
                    event.deleteSignUp(attendee.getDocumentId());
                    event.addCheckIn(attendee.getDocumentId());
                }
            }
        }
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        TextView nameView = view.findViewById(R.id.event_detail_name);
        //TextView organizerView = view.findViewById(R.id.event_detail_organizer);
        TextView locationView = view.findViewById(R.id.event_detail_location);
        TextView descriptionView = view.findViewById(R.id.event_detail_details);
        TextView startDateView = view.findViewById(R.id.event_detail_start_date);
        TextView endDateView = view.findViewById(R.id.event_detail_end_date);
        ImageView posterView = view.findViewById(R.id.posterView);
        ListView announcementListView = view.findViewById(R.id.announcement_list_view);
        Button signUpEvent = view.findViewById(R.id.sign_up_button);
        signUpEvent.setOnClickListener(new View.OnClickListener() {
            // need to get attendeeID and eventID first
            @Override
            public void onClick(View view) {
                event.addSignUp(attendee.getDocumentId());
                FirebaseDB.updateEvent(event);
            }
        });
        Button viewAttendeesButton = view.findViewById(R.id.attendee_list_button);
        String nameString = "Name: "+event.getName();
        //String organizerString = "Organized by: ";
        FirebaseDB.getUserName(event.getOrganizerId(), new FirebaseDB.GetStringCallBack() {
            @Override
            public void onResult(String string) {
                updateOrganizerString(string, view);
            }
        });
        String locationString = "Location: "+event.getLocation();
        String descriptionString = "Description: "+event.getDetails();
        String startDateString = "Starts: "+event.getStartDate();
        String endDateString = "Ends: "+event.getEndDate();


        nameView.setText(nameString);
        //organizerView.setText(organizerString);
        locationView.setText(locationString);
        descriptionView.setText(descriptionString);
        startDateView.setText(startDateString);
        endDateView.setText(endDateString);

        if (event.getPosterPath() != null) {
            FirebaseDB.retrieveImage(event, new FirebaseDB.GetBitmapCallBack() {
                @Override
                public void onResult(Bitmap bitmap) {
                    posterView.setImageBitmap(bitmap);
                }
            });
        }

        ArrayList<String> announcementsList = event.getAnnouncements();
        if (announcementsList == null){
            announcementsList = new ArrayList<String>();
        }
        ArrayAdapter<String> announcementsAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, announcementsList);
        announcementListView.setAdapter(announcementsAdapter);

        // Inflate the layout for this fragment
        return view;
    }
    public static EventDetailsFragment newInstance(Event i, Attendee attendee, Boolean isCheckIn){
        Bundle args = new Bundle();
        args.putSerializable("event", i);
        args.putSerializable("attendee", attendee);
        args.putSerializable("isCheckIn", isCheckIn);

        EventDetailsFragment fragment = new EventDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void updateOrganizerString(String string, View view){
        ((TextView) view.findViewById(R.id.event_detail_organizer)).setText("Organizer: " + string);
    }

    private void setAttendee(Attendee attendee){
        this.attendee = attendee;
    }

}