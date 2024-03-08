package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment} factory method to
 * create an instance of this fragment.
 * It is meant to create a fragment that displays the events
 * the user has either signed up to, or checked into.
 */

public class HomeFragment extends Fragment {
    ListView checkedIn;
    ListView signedUp;

    com.example.qrazyqrsrus.HomeCheckedInListAdapter checkedInListAdapter;
    com.example.qrazyqrsrus.HomeSignedUpListAdapter signedUpListAdapter;

    ArrayList<Event> checkedInEvents = new ArrayList<>();
    ArrayList<Event> signedUpEvents = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void getData(CollectionReference collectionReference, String userDocumentId){
        collectionReference
                .whereArrayContains("signUps", userDocumentId)
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
                                if(document.getData().get("signUps") != null){
                                    signedUpEvents.add(event);
                                    signedUpListAdapter.notifyDataSetChanged();
                                }

                            }
                        } else {
                            Log.d("Events", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment

        if (getArguments() == null){
            System.out.println("No Arguments provided");
            return null;
        }

        final CollectionReference collectionReference = db.collection("Events");
        Attendee attendee = (Attendee) getArguments().getSerializable("user");
        getData(collectionReference, attendee.getDocumentId());


        ListView checkedIn = view.findViewById(R.id.checked_in_events_listview);
        ListView signedUp = view.findViewById(R.id.signed_up_events_listview);




        //FirebaseDB.getAttendeeCheckedInEvents(attendee, checkedInEvents);
        //FirebaseDB.getAttendeeSignedUpEvents(attendee, signedUpEvents);

        checkedInListAdapter = new HomeCheckedInListAdapter(getContext(), checkedInEvents);
        signedUpListAdapter = new HomeSignedUpListAdapter(getContext(), signedUpEvents);
        checkedIn.setAdapter(checkedInListAdapter);
        signedUp.setAdapter(signedUpListAdapter);

        checkedIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable("event", checkedInEvents.get(i));
                args.putSerializable("attendee", attendee);
                Navigation.findNavController(view).navigate(R.id.action_mainMenu_to_eventDetailsFragment, args);
            }
        });

        signedUp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable("event", signedUpEvents.get(i));
                args.putSerializable("attendee", attendee);
                Navigation.findNavController(view).navigate(R.id.action_mainMenu_to_eventDetailsFragment, args);
            }
        });


        return view;
    }

    public static HomeFragment newInstance(Attendee attendee){
        Bundle args = new Bundle();
        args.putSerializable("user", attendee);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}