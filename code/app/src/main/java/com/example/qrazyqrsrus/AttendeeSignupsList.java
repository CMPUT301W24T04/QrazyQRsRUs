// This fragment displays a list of attendees who signed up for an event
package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Shows the list of who is signed up for a specific event
 */
public class AttendeeSignupsList extends Fragment {
    // Define the lists and adapter where the attendee information is held
    ListView attendeeList;
    ArrayList<Attendee> attendeeDataList;
    AttendeeSignUpsListAdapter attendeeListAdapter;
    private FirebaseDB firebaseDB;
    /**
     * When the view is created, retrieve the list of attendees for the event from firestore and show it on a list
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // if not in test use firebase
        if(firebaseDB == null){
            firebaseDB = FirebaseDB.getInstance();
        }
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
        assert bundle != null;
        Event event = (Event) bundle.getSerializable("event");
        assert event != null;
        firebaseDB.getEventSignedUpUsers(event, attendeeDataList, attendeeListAdapter);

        attendeeList.setAdapter(attendeeListAdapter);

        // When the list is clicked, reveal the attendee profile information
        attendeeList.setOnItemClickListener((adapterView, view, i, l) -> {
            // pass attendee in a bundle
            //https://stackoverflow.com/questions/42266436/passing-objects-between-fragments
            Bundle bundle1 = new Bundle();
            Attendee current_attendee = attendeeListAdapter.getItem(i);
            bundle1.putSerializable("attendee", current_attendee);
            Navigation.findNavController(attendeeListLayout).navigate(R.id.action_attendeeSignupsList_to_viewProfileFragment2, bundle1);
        });

        // go back when back button is pressed
        attendeeListLayout.findViewById(R.id.button_back_signups).setOnClickListener(v -> {
            Bundle bundle12 = getArguments();
            Navigation.findNavController(attendeeListLayout).navigate(R.id.action_attendeeSignupsList_to_eventDetailsFragment, bundle12);
        });
        attendeeListLayout.findViewById(R.id.button_view_checkins).setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putSerializable("event", event);
            Navigation.findNavController(attendeeListLayout).navigate(R.id.action_attendeeSignupsList_to_attendeeList2,args);
        });

        return attendeeListLayout;
    }
    public void setFirebaseDB(FirebaseDB instance){
        this.firebaseDB = instance;
    }
}