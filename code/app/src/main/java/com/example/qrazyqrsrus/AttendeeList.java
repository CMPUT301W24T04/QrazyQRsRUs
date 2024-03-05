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

import java.util.ArrayList;
import java.util.HashMap;

import com.example.qrazyqrsrus.Attendee;
import com.example.qrazyqrsrus.AttendeeListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class AttendeeList extends Fragment implements com.example.qrazyqrsrus.AddAttendee.AddAttendeeDialogListener {
    /**
     * Contains a list of attendees for the event
     */
    ListView attendeeList;
    ArrayList<Attendee> attendeeDataList;
    AttendeeListAdapter attendeeListAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addAttendeeToList(Attendee attendee){
        /**
         * Adds an attendee to the list of attendees
         * Inputs: Attendee class
         * Outputs: None
         */
        // add attendee to list and update the list
        attendeeDataList.add(attendee);
        attendeeList.setAdapter(attendeeListAdapter);

        createList(attendeeDataList);
    }

    public void createList(ArrayList<Attendee> attendeeDataList){
        final String TAG = "Sample";
        final CollectionReference collectionReference = db.collection("Attendees");
        for(int i=0;i<attendeeDataList.size();i++) {
            // Retrieving the city name and the province name from the EditText fields
            final String attendeeName = attendeeDataList.get(i).getName(); // attendees[i];
            final String attendee_checkins = attendeeDataList.get(i).getNum_checkins();
            //HASHMAP TO STORE THE DATA IN FORM OF KEY-VALUE PAIRS
            HashMap<String, String> data = new HashMap<>();
            // check if user entered something
            if (attendeeName.length() > 0) {
                // If there’s some data in the EditText field, then we create a new key-value pair.
                data.put("Name", attendeeName);
//                data.put("Number of checkins", attendee_checkins); //.toString()
                // ADD DATA TO THE FIRE STORE
                collectionReference
                        .document(attendeeName)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // These are a method which gets executed when the task is succeeded
                                Log.d(TAG, "Data has been added successfully!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // These are a method which gets executed if there’s any problem
                                Log.d(TAG, "Data could not be added!" + e.toString());
                            }
                        });
            }
        }

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
            FirebaseFirestoreException error) {
                // Clear the old list
                attendeeDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get("Name")));
                    String name = doc.getId();
                    String num_checkins = (String) doc.getData().get("Name");
                    attendeeDataList.add(new Attendee(name, num_checkins)); // Adding the cities and provinces from FireStore
                }
                attendeeListAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // DEFINE VIEW
        View attendeeListLayout = inflater.inflate(R.layout.fragment_attendee_list, container, false);
        //**************************************************************************************************************
        //INITIAL LIST FOR TESTING
        String[] attendees = {
                "John", "Mikail", "Arber", "???"
        };
        String[] num_checkins = {
                "3", "7", "4", "???"
        };
        attendeeDataList = new ArrayList<>();

//         Creates list locally
        for (int i = 0; i < attendees.length; i++) {
            attendeeDataList.add(new Attendee(attendees[i], num_checkins[i]));
        }
        // update attendee list adapter
        attendeeList = attendeeListLayout.findViewById(R.id.attendee_list_view);
        attendeeListAdapter = new AttendeeListAdapter(getActivity(), attendeeDataList);
        attendeeList.setAdapter(attendeeListAdapter);

        attendeeListLayout.findViewById(R.id.button_add_attendee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new com.example.qrazyqrsrus.AddAttendee().show(getActivity().getSupportFragmentManager(), "Add Attendee");
            }
        });

        // FIRESTORE IMPLEMENTATION
        createList(attendeeDataList);

        attendeeListLayout.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(attendeeListLayout).navigate(R.id.action_attendeeList_to_mainMenu);
            }
        });
        return attendeeListLayout; //inflater.inflate(R.layout.fragment_attendee_list, container, false);


//        attendeeListLayout.findViewById(R.id.button_add_attendee).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                findViewById(R.id.button_adding_book).setBackgroundColor(Color.RED);
//                new com.example.crazyqrtest.AddAttendee().show(getActivity().getSupportFragmentManager(), "Add Book");



//        new com.example.crazyqrtest.AddAttendee().show(getSupportFragmentManager(), "Add Book");

        //******************************************************************************************************************************
//        // CHECK IF QR CODE IS SCANNED
//        // Register the launcher and result handler
//        private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
//                result -> {
//                    if(result.getContents() == null) {
//                        Toast.makeText(MyActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(MyActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                    }
//                });
//        // Launch
//        public void onButtonClick(View view) {
//            barcodeLauncher.launch(new ScanOptions());
//        }
        //******************************************************************************************************************************


//        final String TAG = "Sample";
//        final CollectionReference collectionReference = db.collection("Attendees");
//        for(int i=0;i<attendeeDataList.size();i++) {
//            // Retrieving the city name and the province name from the EditText fields
//            final String attendeeName = attendeeDataList.get(i).getName(); // attendees[i];
//            final String attendee_checkins = attendeeDataList.get(i).getNum_checkins();
//            //HASHMAP TO STORE THE DATA IN FORM OF KEY-VALUE PAIRS
//            HashMap<String, String> data = new HashMap<>();
//            // check if user entered something
//            if (attendeeName.length() > 0) {
//                // If there’s some data in the EditText field, then we create a new key-value pair.
//                data.put("Name", attendeeName);
////                data.put("Number of checkins", attendee_checkins); //.toString()
//                // ADD DATA TO THE FIRE STORE
//                collectionReference
//                        .document(attendeeName)
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
//                attendeeDataList.clear();
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    Log.d(TAG, String.valueOf(doc.getData().get("Name")));
//                    String name = doc.getId();
//                    String num_checkins = (String) doc.getData().get("Name");
//                    attendeeDataList.add(new Attendee(name, num_checkins)); // Adding the cities and provinces from FireStore
//                }
//                attendeeListAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
//            }
//        });

        //*********************************************************************************************************************



    }
}