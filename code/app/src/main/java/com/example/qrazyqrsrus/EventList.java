// This fragment displays a list of the events
package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

//import com.example.crazyqrtest.Attendee;
//import com.example.crazyqrtest.AttendeeListAdapter;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Gets database information about events an attendee is signed up for and displays it
 */
public class EventList extends Fragment {  // FIX LATER

    ListView eventList;
    ArrayList<Event> eventDataList;
    com.example.qrazyqrsrus.EventListAdapter eventListAdapter;

    private FirebaseDB firebaseDB;

    // FIREBASE
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(firebaseDB == null){
            firebaseDB = FirebaseDB.getInstance();
        }
        // DEFINE VIEW
        //NOTE: This inflates from the same xml file as MyEventsFragment
        //this is fine, but buttons have the same name
        View eventListLayout = inflater.inflate(R.layout.fragment_event_list, container, false);
        //*************************************************** ***********************************************************
        // datalist to hold event classes
        eventDataList = new ArrayList<>();

        //REMOVE LATER SINCE WE PASS IN THE SPECIFIC COLLECTION WE WANT TO GET DATA ONs
        final CollectionReference collectionReference = db.collection("Events");
        final String TAG = "Sample";

        // update attendee list adapter
        eventList = eventListLayout.findViewById(R.id.event_list_view);
        eventListAdapter = new com.example.qrazyqrsrus.EventListAdapter(getActivity(), eventDataList);
        eventList.setAdapter(eventListAdapter);

        // we get all events from the database, and have it populate the datalist and listadapter
//        FirebaseDB.getInstance().getAllEvents(eventDataList, eventListAdapter);
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

        // back button
        return eventListLayout; //inflater.inflate(R.layout.fragment_attendee_list, container, false);




    }
    public void setFirebaseDB(FirebaseDB instance){
        this.firebaseDB = instance;
    }
}