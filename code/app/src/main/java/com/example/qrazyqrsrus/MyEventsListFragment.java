package com.example.qrazyqrsrus;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A {@link Fragment} subclass for displaying a list of events that the current user has either created or signed up for.
 * This fragment is responsible for fetching and displaying the relevant events in a list format, allowing the user
 * to view details about each event or to navigate to the event creation screen.
 *
 * The fragment inflates a layout used for displaying any list of events but customizes it to show only those related
 * to the current user. It leverages {@link EventListAdapter} to populate the list view with event data.
 */
public class MyEventsListFragment extends Fragment {

    private Attendee attendee;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view. Here, it inflates the layout, customizes
     * the header to indicate these are the user's events, and sets up the list and floating action button.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //NOTE: this inflates from the same xml file as the event list to browse all events
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        // Inflate the layout for this fragment

        TextView header = rootView.findViewById(R.id.event_list_title);
        header.setText(R.string.my_events_fragment_header);


        ListView eventListView = rootView.findViewById(R.id.event_list_view);


        ArrayList<Event> myEvents = new ArrayList<>();

        EventListAdapter myEventsListAdapter = new EventListAdapter(getContext(), myEvents);

        if (getArguments() == null){
            FirebaseDB.getInstance().loginUser(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID), new FirebaseDB.GetAttendeeCallBack() {
                @Override
                public void onResult(Attendee attendee) {
                    FirebaseDB.getInstance().getEventsMadeByUser(attendee, myEvents, myEventsListAdapter);
                    setAttendee(attendee);
//                    FirebaseDB.getInstance().getAttendeeSignedUpEvents(attendee, signedUpEvents, homeSignedUpListAdapter);
                }
            });
        } else{
            Attendee attendee = (Attendee) getArguments().getSerializable("attendee");
            setAttendee(attendee);
            FirebaseDB.getInstance().getEventsMadeByUser(attendee, myEvents, myEventsListAdapter);
//            FirebaseDB.getInstance().getAttendeeSignedUpEvents(attendee, signedUpEvents, homeSignedUpListAdapter);
        }

        eventListView.setAdapter(myEventsListAdapter);


        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable("event", myEvents.get(i));
//                args.putSerializable("attendee", attendee);
                Navigation.findNavController(rootView).navigate(R.id.action_myEventsListFragment_to_eventDetailsFragment, args);
            }
        });

        FloatingActionButton fab = rootView.findViewById(R.id.new_event_button);
        fab.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putSerializable("attendee", this.attendee);
            args.putSerializable("builder", new Event.EventBuilder());
            Navigation.findNavController(rootView).navigate(R.id.action_myEventsListFragment_to_newEventTextFragment, args);
        });


        return rootView;
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param attendee The attendee associated with the events to display.
     * @return A new instance of fragment MyEventsListFragment.
     */
    public static HomeFragment newInstance(Attendee attendee){
        Bundle args = new Bundle();
        args.putSerializable("user", attendee);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Sets the {@link Attendee} for whom the events are to be displayed.
     * This method is utilized to update the fragment with the attendee's information
     * when it is available after creation or upon successful login.
     *
     * @param attendee The {@link Attendee} whose events are to be displayed.
     */
    private void setAttendee(Attendee attendee){
        this.attendee = attendee;
    }
}
