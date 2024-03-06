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

public class EventList extends Fragment {  // FIX LATER
    /**
     * Contains a list of attendees for the event
     */
    ListView eventList;
    ArrayList<Event> eventDataList;
    com.example.qrazyqrsrus.EventListAdapter eventListAdapter;

    // FIREBASE
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addEventToList(Event event){
        /**
         * Adds an attendee to the list of attendees
         * Inputs: Attendee class
         * Outputs: None
         */
        // add attendee to list and update the list
        eventDataList.add(event);
        eventList.setAdapter(eventListAdapter);

        createList(eventDataList);
    }

    public void createList(ArrayList<Event> eventDataList){
//        /**
//         * Creates the firebase list
//         */
//        final String TAG = "Sample";
//        final CollectionReference collectionReference = db.collection("_Events_");
//        for(int i=0;i<eventDataList.size();i++) {
//            // Retrieving the city name and the province name from the EditText fields
//            final String eventName = eventDataList.get(i).getEventName(); //events[i];
//            final String eventLocation = eventDataList.get(i).getLocation(); //locations[i];
//            final String eventDate = eventDataList.get(i).getDate(); //dates[i];
//            final String eventDetail = eventDataList.get(i).getDetails(); //details[i];
//            //HASHMAP TO STORE THE DATA IN FORM OF KEY-VALUE PAIRS
//            HashMap<String, String> data = new HashMap<>();
//            // check if user entered something
//            if (eventName.length() > 0) {
//                // If there’s some data in the EditText field, then we create a new key-value pair.
//                data.put("Name", eventName);
//                data.put("Location", eventLocation);
//                data.put("Date", eventDate);
//                data.put("Detail", eventDetail);
//
////                data.put("Number of checkins", attendee_checkins); //.toString()
//                // ADD DATA TO THE FIRE STORE
//                collectionReference
//                        .document(eventName)
//                        .set(data)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // These are a method which gets executed when the task is succeeded
//                                Log.d(TAG, "Data has been added successfully!");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // These are a method which gets executed if there’s any problem
//                                Log.d(TAG, "Data could not be added!" + e.toString());
//                            }
//                        });
//            }
//        }
//
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//            FirebaseFirestoreException error) {
//                // Clear the old list
//                eventDataList.clear();
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    Log.d(TAG, String.valueOf(doc.getData().get("Name")));
//                    String name = doc.getId();
//                    String location = (String) doc.getData().get("Location");
//                    String date = (String) doc.getData().get("Date");
//                    String detail = (String) doc.getData().get("Detail");
//                    eventDataList.add(new Event(name, location, date, detail)); // Adding the cities and provinces from FireStore
//                }
//                eventListAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
//            }
//        });
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
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // DEFINE VIEW
        View eventListLayout = inflater.inflate(R.layout.fragment_event_list, container, false);
        //**************************************************************************************************************

        eventDataList = new ArrayList<>();
        final CollectionReference collectionReference = db.collection("Events");
        final String TAG = "Sample";

        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Events", "Retrieved all events");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String eventName = (String) document.getData().get("name");
                                String organizerId = (String) document.getData().get("organizerId");
                                String details = (String) document.getData().get("details");
                                String location = (String) document.getData().get("location");
                                //LocalDateTime startDate = (LocalDateTime) document.getData().get("startDate");    FIX LATER
                                LocalDateTime startDate = (LocalDateTime) document.getData().get("startDate");
                                LocalDateTime endDate = (LocalDateTime) document.getData().get("endDate");
                                Boolean geolocationOn = (Boolean) document.getData().get("geolocationOn");
                                String posterPath = (String) document.getData().get("posterPath");
                                String qrCodePath = (String) document.getData().get("qrCodePath");
                                String qrCodePromoPath = (String) document.getData().get("qrCodePromoPath");
                                ArrayList<String> announcements = (ArrayList<String>) document.getData().get("announcements");
                                ArrayList<String> signUps = (ArrayList<String>) document.getData().get("signUps");
                                ArrayList<Map<String, Object>> checkIns = (ArrayList<Map<String, Object>>) document.getData().get("checkIns");


                                Event event = new Event(eventName, organizerId, details, location, startDate, endDate); // new Event(eventName, location, startDate, details,);  //(id, documentId, name, email, profilePicturePath, geolocationOn);
                                eventDataList.add(event);
                                eventListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Events", "Error getting documents: ", task.getException());
                        }
                    }
                });

        // update attendee list adapter
        eventList = eventListLayout.findViewById(R.id.event_list_view);
        eventListAdapter = new com.example.qrazyqrsrus.EventListAdapter(getActivity(), eventDataList);
        eventList.setAdapter(eventListAdapter);

        // FIRESTORE IMPLEMENTATION
//        createList(eventDataList);

        eventListLayout.findViewById(R.id.event_button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(eventListLayout).navigate(R.id.action_eventList_to_mainMenu);
            }
        });
        return eventListLayout; //inflater.inflate(R.layout.fragment_attendee_list, container, false);

//        new com.example.crazyqrtest.AddAttendee().show(getSupportFragmentManager(), "Add Book");
//        final String TAG = "Sample";
//        final CollectionReference collectionReference = db.collection("Events");
//        for(int i=0;i<eventDataList.size();i++) {
//            // Retrieving the city name and the province name from the EditText fields
//            final String eventName = eventDataList.get(i).getEventName(); //events[i];
//            final String eventLocation = eventDataList.get(i).getLocation(); //locations[i];
//            final String eventDate = eventDataList.get(i).getDate(); //dates[i];
//            final String eventDetail = eventDataList.get(i).getDetails(); //details[i];
//            //HASHMAP TO STORE THE DATA IN FORM OF KEY-VALUE PAIRS
//            HashMap<String, String> data = new HashMap<>();
//            // check if user entered something
//            if (eventName.length() > 0) {
//                // If there’s some data in the EditText field, then we create a new key-value pair.
//                data.put("Name", eventName);
//                data.put("Location", eventLocation);
//                data.put("Date", eventDate);
//                data.put("Detail", eventDetail);
//
////                data.put("Number of checkins", attendee_checkins); //.toString()
//                // ADD DATA TO THE FIRE STORE
//                collectionReference
//                        .document(eventName)
//                        .set(data)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // These are a method which gets executed when the task is succeeded
//                                Log.d(TAG, "Data has been added successfully!");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // These are a method which gets executed if there’s any problem
//                                Log.d(TAG, "Data could not be added!" + e.toString());
//                            }
//                        });
//            }
//        }
//
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//            FirebaseFirestoreException error) {
//                // Clear the old list
//                eventDataList.clear();
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    Log.d(TAG, String.valueOf(doc.getData().get("Name")));
//                    String name = doc.getId();
//                    String location = (String) doc.getData().get("Location");
//                    String date = (String) doc.getData().get("Date");
//                    String detail = (String) doc.getData().get("Detail");
//                    eventDataList.add(new Event(name, location, date, detail)); // Adding the cities and provinces from FireStore
//                }
//                eventListAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
//            }
//        });

        //*********************************************************************************************************************



    }
}