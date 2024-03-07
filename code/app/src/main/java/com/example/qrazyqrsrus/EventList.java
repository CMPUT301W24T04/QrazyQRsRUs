package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Gets database information about event attendee is signed up for and displays it
 */
public class EventList extends Fragment {  // FIX LATER

    ListView eventList;
    ArrayList<Event> eventDataList;
    com.example.qrazyqrsrus.EventListAdapter eventListAdapter;

    // FIREBASE
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Adds data from firebase to the list of events to be displayed
     * @param collectionReference
     */
    private void getData(CollectionReference collectionReference){
        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Events", "Retrieved all events");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                String eventName = (String) document.getData().get("name");
                                String organizerId = (String) document.getData().get("organizerId");
                                String details = (String) document.getData().get("details");
                                String location = (String) document.getData().get("location");
                                //LocalDateTime startDate = (LocalDateTime) document.getData().get("startDate");    FIX LATER

                                String startDate = (String) document.getData().get("startDate");
                                String endDate = (String) document.getData().get("endDate");

                                Boolean geolocationOn = (Boolean) document.getData().get("geolocationOn");
                                String posterPath = (String) document.getData().get("posterPath");
                                String qrCodePath = (String) document.getData().get("qrCodePath");
                                String qrCodePromoPath = (String) document.getData().get("qrCodePromoPath");
                                ArrayList<String> announcements = (ArrayList<String>) document.getData().get("announcements");
                                ArrayList<String> signUps = (ArrayList<String>) document.getData().get("signUps");
                                ArrayList<String> checkIns = (ArrayList<String>) document.getData().get("checkIns"); //ArrayList<Map<String, Object>> checkIns = (ArrayList<Map<String, Object>>) document.getData().get("checkIns");

                                Event event = new Event(documentId, eventName, organizerId, details,
                                        location, startDate, endDate,
                                        geolocationOn, posterPath, qrCodePath,
                                        qrCodePromoPath, announcements, signUps, checkIns);

                                // event = new Event(eventName, organizerId, details, location, startDate, endDate); // new Event(eventName, location, startDate, details,);  //(id, documentId, name, email, profilePicturePath, geolocationOn);
                                eventDataList.add(event);
                                eventListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Events", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

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
        // DEFINE VIEW
        View eventListLayout = inflater.inflate(R.layout.fragment_event_list, container, false);
        //*************************************************** ***********************************************************
        // datalist to hold event classes
        eventDataList = new ArrayList<>();

        //REMOVE LATER SINCE WE PASS IN THE SPECIFIC COLLECTION WE WANT TO GET DATA ONs
        final CollectionReference collectionReference = db.collection("Events");
        final String TAG = "Sample";

        // get data from the collection reference we pass in function
        getData(collectionReference);


        // update attendee list adapter
        eventList = eventListLayout.findViewById(R.id.event_list_view);
        eventListAdapter = new com.example.qrazyqrsrus.EventListAdapter(getActivity(), eventDataList);
        eventList.setAdapter(eventListAdapter);

        FloatingActionButton fab = eventListLayout.findViewById(R.id.new_event_button);
        fab.setOnClickListener(v -> {
            Navigation.findNavController(eventListLayout).navigate(R.id.action_eventList2_to_newEventTextFragment);
        });

        // When list is clicked, go to event view with event information
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // pass event in a bundle
                //https://stackoverflow.com/questions/42266436/passing-objects-between-fragments
                Bundle bundle = new Bundle();
                Event current_event = eventListAdapter.getItem(i);
                bundle.putSerializable("current_event", current_event);
//
//                //turn the textviews into the desired names based on the name lists
//                Name.setText(attendee_value);
                Navigation.findNavController(eventListLayout).navigate(R.id.action_eventList_to_eventInfoView, bundle);
            }
        });

        // back button
        eventListLayout.findViewById(R.id.event_button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(eventListLayout).navigate(R.id.action_eventList_to_mainMenu);
            }
        });
        return eventListLayout; //inflater.inflate(R.layout.fragment_attendee_list, container, false);




    }
}