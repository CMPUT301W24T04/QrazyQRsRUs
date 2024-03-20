package com.example.qrazyqrsrus;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AdminViewEventsFragment extends Fragment {

    private TextView nameView;
    private TextView organizerView;
    private TextView locationView;
    private TextView descriptionView;
    private TextView startDateView;
    private TextView endDateView;
    private ImageView posterView;
    private Button deleteButton;
    private Button cancelButton;
    private Button confirmButton;
    private TextView confirmTextView;
    private Button nextButton;
    private Button previousButton;



    private ArrayList<Event> allEvents = new ArrayList<>();
    private Integer currentPosition;

    public AdminViewEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_view_events, container, false);

        nameView = rootView.findViewById(R.id.event_detail_name);
        organizerView = rootView.findViewById(R.id.event_detail_organizer);
        locationView = rootView.findViewById(R.id.event_detail_location);
        descriptionView = rootView.findViewById(R.id.event_detail_details);
        startDateView = rootView.findViewById(R.id.event_detail_start_date);
        endDateView = rootView.findViewById(R.id.event_detail_end_date);
        posterView = rootView.findViewById(R.id.posterView);
        Button viewAnnouncementsButton = rootView.findViewById(R.id.view_announcements_button);
        FloatingActionButton backButton = rootView.findViewById(R.id.back_button);
        deleteButton = rootView.findViewById(R.id.delete_button);
        cancelButton = rootView.findViewById(R.id.cancel_button);
        confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmTextView = rootView.findViewById(R.id.confirm_text_view);
        nextButton = rootView.findViewById(R.id.next_event_button);
        previousButton = rootView.findViewById(R.id.previous_event_button);

        currentPosition = 0;
        FirebaseDB.getAllEvents(new FirebaseDB.GetAllEventsCallBack() {
            @Override
            public void onResult(ArrayList<Event> events) {
                allEvents = new ArrayList<>(events);
                updateView();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Navigation.findNavController(rootView).popBackStack();
                } catch (Exception e){
                    backButton.setVisibility(View.GONE);
                }
            }
        });
        viewAnnouncementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable("event", allEvents.get(currentPosition));
                Navigation.findNavController(rootView).navigate(R.id.action_adminViewEventsFragment_to_announcementsFragment, args);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDB.deleteEvent(allEvents.get(currentPosition));
                FirebaseDB.getAllEvents(new FirebaseDB.GetAllEventsCallBack() {
                    @Override
                    public void onResult(ArrayList<Event> events) {
                        allEvents = new ArrayList<>(events);
                        updateView();
                        changeState();
                    }
                });
                if (currentPosition != 0) {
                    currentPosition -= 1;
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition != allEvents.size() - 1) {
                    currentPosition += 1;
                    updateView();
                }
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition != 0) {
                    currentPosition -= 1;
                    updateView();
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void updateView() {
        Event currentEvent = allEvents.get(currentPosition);
        String nameString = "Name: "+currentEvent.getName();
        String organizerString = "Organized by: ";
        String locationString = "Location: "+currentEvent.getLocation();
        String descriptionString = "Description: "+currentEvent.getDetails();
        String startDateString = "Starts: "+currentEvent.getStartDate();
        String endDateString = "Ends: "+currentEvent.getEndDate();
        FirebaseDB.getUserName(currentEvent.getOrganizerId(), new FirebaseDB.GetStringCallBack() {
            @Override
            public void onResult(String string) {
                organizerView.setText(organizerString + string);
            }
        });
        nameView.setText(nameString);
        locationView.setText(locationString);
        descriptionView.setText(descriptionString);
        startDateView.setText(startDateString);
        endDateView.setText(endDateString);

        if (currentEvent.getPosterPath() != null) {
            FirebaseDB.retrieveImage(currentEvent, new FirebaseDB.GetBitmapCallBack() {
                @Override
                public void onResult(Bitmap bitmap) {
                    posterView.setImageBitmap(bitmap);
                }
            });
        }
        else {
            posterView.setImageResource(R.drawable.no_image_source);
        }
    }

    public void changeState() {
        deleteButton.setVisibility(deleteButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        cancelButton.setVisibility(cancelButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        confirmButton.setVisibility(confirmButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        confirmTextView.setVisibility(confirmTextView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
}