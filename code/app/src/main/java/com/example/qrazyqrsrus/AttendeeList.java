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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Shows the list of attendees for the event
 */
public class AttendeeList extends Fragment {
    // Define the lists and adapter where the attendee information is held
    ListView attendeeList;
    ArrayList<Attendee> attendeeDataList;
    AttendeeListAdapter attendeeListAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Gets attendees from a specific database based on its collection reference called
     * @param collectionReference
     */
    private void getData(CollectionReference collectionReference){
        // get attendee information and add it to a list of attendees for viewing
        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Attendees", "Retrieved all Attendees");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //
                                if(document.getData().size()==0){
                                    String documentId = document.getId();
                                    String id = (String) document.getData().get("id");
                                    Attendee attendee = new Attendee("No Name", documentId, id);
                                    attendeeDataList.add(attendee);
                                    attendeeListAdapter.notifyDataSetChanged();
                                }
                                if(document.getData().size()==1){
                                    String documentId = document.getId();
                                    String id = (String) document.getData().get("id");
                                    Attendee attendee = new Attendee("No Name", documentId, id);
                                    attendeeDataList.add(attendee);
                                    attendeeListAdapter.notifyDataSetChanged();
                                }
                                else{
                                    String documentId = document.getId();
                                    String id = (String) document.getData().get("id");
                                    String name = (String) document.getData().get("name");
                                    String email = (String) document.getData().get("email");
                                    String profilePicturePath = (String) document.getData().get("profilePicturePath");
                                    Boolean geolocationOn = (Boolean) document.getData().get("geolocationOn");
                                    Attendee attendee = new Attendee(id, documentId, name, email, profilePicturePath, geolocationOn);  //(id, documentId, name, email, profilePicturePath, geolocationOn);
                                    attendeeDataList.add(attendee);
                                    attendeeListAdapter.notifyDataSetChanged();
                                }

                            }
                        } else {
                            Log.d("Attendees", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * When the view is created, retrive the list of attendees for the event from firestore and show it on a list
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Creates the view for the list of attendees
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // DEFINE VIEW
        View attendeeListLayout = inflater.inflate(R.layout.fragment_attendee_list, container, false);
        //**************************************************************************************************************
        //INITIAL LIST FOR TESTING
        attendeeDataList = new ArrayList<>();
        final CollectionReference collectionReference = db.collection("Users");
        final String TAG = "Sample";

        // call getData from the firestore to populate the list
//        getData(collectionReference);


        // update attendee list and shows it on the listview
        attendeeList = attendeeListLayout.findViewById(R.id.attendee_list_view);
        attendeeListAdapter = new AttendeeListAdapter(getActivity(), attendeeDataList);

        // populate the attendees list
        FirebaseDB.getAllUsers(attendeeDataList, attendeeListAdapter);
        attendeeList.setAdapter(attendeeListAdapter);

        // When the list is clicked, reveal the attendee profile information
        attendeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // pass attendee in a bundle
                //https://stackoverflow.com/questions/42266436/passing-objects-between-fragments
                Bundle bundle = new Bundle();
                Attendee current_attendee = attendeeListAdapter.getItem(i);
                bundle.putSerializable("current_attendee", current_attendee);
//                attendee_info.setArguments(bundle);

//                startActivity(i);
//
//                //turn the textviews into the desired names based on the name lists
//                Name.setText(attendee_value);
                Navigation.findNavController(attendeeListLayout).navigate(R.id.action_attendeeList_to_attendeeInfoView,bundle);
            }
        });

        // go back when back button is pressed
        attendeeListLayout.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(attendeeListLayout).navigate(R.id.action_attendeeList2_to_eventDetailsFragment);
            }
        });
        return attendeeListLayout; //inflater.inflate(R.layout.fragment_attendee_list, container, false);


    }
}
