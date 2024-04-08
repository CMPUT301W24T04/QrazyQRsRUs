// This fragment displays a list of the events
package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
/**
 * Gets database information about events an attendee is signed up for and displays it
 */
public class EventList extends Fragment {

    ListView eventList;
    ArrayList<Event> eventDataList;
    com.example.qrazyqrsrus.EventListAdapter eventListAdapter;

    private FirebaseDB firebaseDB;
    /**
     * Gets data from firestore and displays it on a listview
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(firebaseDB == null){
            firebaseDB = FirebaseDB.getInstance();
        }
        // DEFINE VIEW
        //NOTE: This inflates from the same xml file as MyEventsFragment
        //this is fine, but buttons have the same name
        View eventListLayout = inflater.inflate(R.layout.fragment_event_list, container, false);
        // data-list to hold event classes
        eventDataList = new ArrayList<>();


        // update attendee list adapter
        eventList = eventListLayout.findViewById(R.id.event_list_view);
        eventListAdapter = new com.example.qrazyqrsrus.EventListAdapter(getActivity(), eventDataList);
        eventList.setAdapter(eventListAdapter);

        // we get all events from the database, and have it populate the data-list and listadapter
        firebaseDB.getAllEvents(eventDataList, eventListAdapter);

        //set the header to say "browse all events"
        TextView header = eventListLayout.findViewById(R.id.event_list_title);
        header.setText(R.string.browse_events_header);

        //hide the fab from the signed up event list layout
        FloatingActionButton fab = eventListLayout.findViewById(R.id.new_event_button);
        fab.setVisibility(View.GONE);

        // When list is clicked, go to event view with event information
        eventList.setOnItemClickListener((adapterView, view, i, l) -> {
            // pass event in a bundle
            //https://stackoverflow.com/questions/42266436/passing-objects-between-fragments
            Bundle bundle = new Bundle();
            Event current_event = eventListAdapter.getItem(i);
            bundle.putSerializable("event", current_event);
            Navigation.findNavController(eventListLayout).navigate(R.id.action_eventList3_to_eventDetailsFragment3, bundle);
        });
        return eventListLayout;
    }
    public void setFirebaseDB(FirebaseDB instance){
        this.firebaseDB = instance;
    }
}