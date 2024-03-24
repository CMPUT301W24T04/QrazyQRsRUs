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
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Shows the list of attendees for the event
 */
public class AttendeeSignupsList extends Fragment {
    // Define the lists and adapter where the attendee information is held
    ListView attendeeList;
    ArrayList<Attendee> attendeeDataList;
    AttendeeSignUpsListAdapter attendeeListAdapter;
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
        View attendeeListLayout = inflater.inflate(R.layout.fragment_attendee_signups_list, container, false);
        //**************************************************************************************************************
        //INITIAL LIST FOR TESTING
        attendeeDataList = new ArrayList<>();

        // call getData from the firestore to populate the list
//        getData(collectionReference);

        // update attendee list and shows it on the listview
        attendeeList = attendeeListLayout.findViewById(R.id.attendee_signups_list_view);
        attendeeListAdapter = new AttendeeSignUpsListAdapter(getActivity(), attendeeDataList);

        //https://stackoverflow.com/questions/42266436/passing-objects-between-fragments
        Bundle bundle = getArguments();
        Event event = (Event) bundle.getSerializable("event");
        FirebaseDB.getEventSignedUpUsers(event, attendeeDataList, attendeeListAdapter);
        //FirebaseDB.getEventCheckedIn(event, attendeeDataList, attendeeListAdapter);

        //see who is signed up for the event as well
//        for(Integer i = 0; i < attendeeDataList.size(); i++){
//            // check if the checked-in user is also signed-up
//            if(event.getSignUps().contains(attendeeDataList.get(i))){
//                Attendee current_attendee = attendeeDataList.get(i);
//                // set this attendee to signed_up for the event
//                current_attendee.setSignedup(true);
//            }
//            else{
//                Attendee current_attendee = attendeeDataList.get(i);
//                // set this attendee to not signed_up for the event
//                current_attendee.setSignedup(false);
//            }
//        }

        //TODO: REMOVE PEOPLE SIGNED UP FROM PEOPLE CHECKED-IN
        //



        // populate the attendees list
        //FirebaseDB.getAllUsers(attendeeDataList, attendeeListAdapter);
        attendeeList.setAdapter(attendeeListAdapter);

        // When the list is clicked, reveal the attendee profile information
//        attendeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                // pass attendee in a bundle
//                //https://stackoverflow.com/questions/42266436/passing-objects-between-fragments
//                Bundle bundle = new Bundle();
//                Attendee current_attendee = attendeeListAdapter.getItem(i);
//                bundle.putSerializable("current_attendee", current_attendee);
////                attendee_info.setArguments(bundle);
//
////                startActivity(i);
////
////                //turn the textviews into the desired names based on the name lists
////                Name.setText(attendee_value);
//                Navigation.findNavController(attendeeListLayout).navigate(R.id.action_attendeeList_to_attendeeInfoView,bundle);
//            }
//        });

        // go back when back button is pressed
        attendeeListLayout.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(attendeeListLayout).navigate(R.id.action_attendeeList2_to_eventDetailsFragment);
            }
        });
        attendeeListLayout.findViewById(R.id.button_view_checkins).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable("event", event);
                Navigation.findNavController(attendeeListLayout).navigate(R.id.action_attendeeSignupsList_to_attendeeList2,args);
            }
        });

        return attendeeListLayout; //inflater.inflate(R.layout.fragment_attendee_list, container, false);
    }
}