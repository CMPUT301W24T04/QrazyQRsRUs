package com.example.qrazyqrsrus;

// This class shows a list of events created by the user
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
 * gets list of attendees
 */
public class MyEventsListFragment extends Fragment {
    /**
     * Saves the bundle passed to the class
     */
    private Attendee attendee;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Shows a list of events when the view is created
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return rootView
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

                @Override
                public void onNoResult() {
                    new ErrorDialog(R.string.login_error).show(getActivity().getSupportFragmentManager(), "Error Dialog");
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
     * Creates a bundle for the attendee
     * @param attendee
     * @return fragment
     */
    public static HomeFragment newInstance(Attendee attendee){
        Bundle args = new Bundle();
        args.putSerializable("user", attendee);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Constructor for the attendee
     * @param attendee
     */
    private void setAttendee(Attendee attendee){
        this.attendee = attendee;
    }
}
