package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.List;

import com.example.qrazyqrsrus.Attendee;
import com.example.qrazyqrsrus.AttendeeListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class AttendeeList extends Fragment {
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

    public void createList(ArrayList<Attendee> attendeeDataList) {
        final String TAG = "Sample";
        final CollectionReference collectionReference = db.collection("Attendees");
        HashMap<String, String> data = new HashMap<>();
        collectionReference
                .document("111")
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
//        for (Attendee attendee : attendeeDataList) { // iterate through attendees in attendee list     //for(int i=0;i<attendeeDataList.size();i++)
//            // Retrieving the city name and the province name from the EditText fields
////            final String attendeeName = attendee.getName() //.get(i).getName(); // attendees[i];
////            final String attendee_checkins = attendeeDataList.get(i).getNum_checkins();
//            //HASHMAP TO STORE THE DATA IN FORM OF KEY-VALUE PAIRS
////            HashMap<String, String> data = new HashMap<>();
//            // check if user entered something
////            if (attendeeName.length() > 0) {
////                // If there’s some data in the EditText field, then we create a new key-value pair.
////                data.put("Name", attendeeName); //add
////                data.put("Number of checkins", attendee_checkins); //.toString()
//            // ADD DATA TO THE FIRE STORE
//            collectionReference
//                    // add attendee to database
//                    .document("added new document");
////                    .add(attendee)
////                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
////                        @Override
////                        public void onSuccess(DocumentReference documentReference) {
////                            Log.d(TAG, "Data has been added successfully!");
////                        }
////                    })
////                    .addOnFailureListener(new OnFailureListener() {
////                        @Override
////                        public void onFailure(@NonNull Exception e) {
////                            Log.d(TAG, "Data could not be added!" + e.toString());
////                        }
////                    });
//        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // DEFINE VIEW
        View attendeeListLayout = inflater.inflate(R.layout.fragment_attendee_list, container, false);
        //**************************************************************************************************************
        //INITIAL LIST FOR TESTING
//        String[] attendees = {
//                "John", "Mikail", "Arber", "???", "Ikjyot"
//        };
//        String[] num_checkins = {
//                "3", "7", "4", "???", "123"
//        };
        attendeeDataList = new ArrayList<>();
        final CollectionReference collectionReference = db.collection("Attendees");
        final String TAG = "Sample";

//        db.collection("Attendees")
//                .whereEqualTo("user-uid",uid)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(getActivity(),"success accessing database",Toast.LENGTH_SHORT).show();
//                            for (QueryDocumentSnapshot document : task.getResult()){
//                                //Fetch from database as Map
//                                user_name = (String) document.getData().get("user-name");
//                                user_last_name =(String) document.getData().get("user-last-name");
//                                user_phone_number =(String) document.getData().get("user-phone-number");
//                                user_email =(String) document.getData().get("user-email");
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
//                attendeeDataList.clear();
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    Log.d(TAG, String.valueOf(doc.getData().get("Name")));
//                    String name = doc.getId();
//                    String num_checkins = (String) doc.getData().get("Name");
//                    attendeeDataList.add(new Attendee(name, num_checkins)); // Add data from firestore
//                }
//                attendeeListAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
//            }
//        });

        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Users", "Retrieved all users");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                String id = (String) document.getData().get("id");
                                String name = (String) document.getData().get("name");
                                String email = (String) document.getData().get("email");
                                String profilePicturePath = (String) document.getData().get("profilePicturePath");
                                Boolean geolocationOn = (Boolean) document.getData().get("geolocationOn");


                                 Attendee attendee = new Attendee(name, id);  //(id, documentId, name, email, profilePicturePath, geolocationOn);
                                 attendeeDataList.add(attendee);
                                 attendeeListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Users", "Error getting documents: ", task.getException());
                        }
                    }
                });

//         Creates list locally for testing
//        for (int i = 0; i < attendees.length; i++) {
//            attendeeDataList.add(new Attendee(attendees[i], num_checkins[i]));
//        }
        // update attendee list adapter
        attendeeList = attendeeListLayout.findViewById(R.id.attendee_list_view);
        attendeeListAdapter = new AttendeeListAdapter(getActivity(), attendeeDataList);
        attendeeList.setAdapter(attendeeListAdapter);

        attendeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //change the view to city_content fragment
                //get the textview id from city_content fragment
//                ConstraintLayout layout = (ConstraintLayout) attendeeListLayout.findViewById(R.id.attendee_info_view);
//                TextView Name = attendeeListLayout.findViewById(R.id.attendee_name);
//                Attendee current_attendee = attendeeListAdapter.getItem(i);
//                String attendee_value = current_attendee.getName();
//
//                //turn the textviews into the desired names based on the name lists
//                Name.setText(attendee_value);
                Navigation.findNavController(attendeeListLayout).navigate(R.id.action_attendeeList_to_attendeeInfoView);
            }
        });

//        attendeeListLayout.findViewById(R.id.button_add_attendee).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new com.example.qrazyqrsrus.AddAttendee().show(getActivity().getSupportFragmentManager(), "Add Attendee");
//            }
//        });

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
//        public static void addUser(String user) {
//            usersCollection
//                    .add(user)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.d(usersTAG, "User document snapshot written with ID:" + documentReference.getId());
//                            // user.setDocumentId(documentReference.getId())
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(usersTAG, "Error while adding user document", e);
//                        }
//                    });
//        }
//
//        //******************************************************************************************************************************
//        // CHECK IF QR CODE IS SCANNED
//        // Register the launcher and result handler
//        private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
//                result -> {
//                    if(result.getContents() == null) {
//                        Toast.makeText(MyActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(MyActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                        usersCollection
//                                .whereEqualTo("id", userId)
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            int i = 0;
//                                            for (DocumentSnapshot documentSnapshot: task.getResult()) {
//                                                // user = documentSnapshot.toObject(Attendee.class);
//                                                i += 1;
//                                            }
//                                            if (i == 0) {
//                                                // This means that Attendee needs a constructor where it only accepts userId and sets the rest to default
//                                                // user = new Attendee(userId);
//                                                // addUser(user);
//                                            }
//                                        }
//                                        else {
//                                            Log.e("MainActivity", "Error trying to login");
//                                        }
//                                    }
//                                });
//                    }
//                });

        // Launch
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