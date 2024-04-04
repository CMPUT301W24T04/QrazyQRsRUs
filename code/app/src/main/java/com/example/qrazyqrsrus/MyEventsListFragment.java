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
 * gets list of attendees
 */
public class MyEventsListFragment extends Fragment {

    private Attendee attendee;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //NOTE: this inflates from the same xml file as the event list to browse all events
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        // Inflate the layout for this fragment

        TextView header = rootView.findViewById(R.id.event_list_title);
        header.setText(R.string.my_events_fragment_header);


        ListView eventListView = rootView.findViewById(R.id.event_list_view);
//        ListView signedUp = rootView.findViewById(R.id.signed_up_events_listview);
//        browseEvents = rootView.findViewById(R.id.browse_events_button);

        ArrayList<Event> myEvents = new ArrayList<>();
//        ArrayList<Event> signedUpEvents = new ArrayList<>();
        EventListAdapter myEventsListAdapter = new EventListAdapter(getContext(), myEvents);
//        HomeSignedUpListAdapter homeSignedUpListAdapter = new HomeSignedUpListAdapter(getContext(), signedUpEvents);

        //if the attendee is not passed, we must get the attendee to display only the events they are in.
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

//        signedUp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Bundle args = new Bundle();
//                args.putSerializable("event", signedUpEvents.get(i));
//                args.putSerializable("attendee", attendee);
////                NavHostFragment navHost = (NavHostgetView().findViewById(R.id.nav_graph_nav_host);
////                Navigation.findNavController(getView()).navigate(R.id.action_mainMenu_to_eventDetailsFragment, args);
//                Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_eventDetailsFragment3, args);
//            }
//        });
//
//        browseEvents.setOnClickListener(v -> {
//            Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_eventList3);
//        });

        return rootView;
    }

    public static HomeFragment newInstance(Attendee attendee){
        Bundle args = new Bundle();
        args.putSerializable("user", attendee);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void setAttendee(Attendee attendee){
        this.attendee = attendee;
    }
}
