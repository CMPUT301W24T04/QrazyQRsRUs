package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class HomeFragment extends Fragment{
    ListView checkedIn;
    ListView signedUp;
    private Attendee attendee;

    com.example.qrazyqrsrus.HomeCheckedInListAdapter checkedInListAdapter;
    com.example.qrazyqrsrus.HomeSignedUpListAdapter signedUpListAdapter;

    ArrayList<Event> checkedInEvents = new ArrayList<>();
    ArrayList<Event> signedUpEvents = new ArrayList<>();

    FloatingActionButton browseEvents;

    /**
     * Required empty public constructor for the fragment.
     */
    public HomeFragment() {
        // Required empty public constructor
    }


    /**
     * Invoked when the fragment is being created. Handles the initialization of the fragment.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this
     *                           is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view. This method inflates
     * the layout for the fragment's view and initializes the list views and adapters for
     * displaying checked-in and signed-up events. It sets up event handlers for user interactions.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment


        ListView checkedIn = rootView.findViewById(R.id.checked_in_events_listview);
        ListView signedUp = rootView.findViewById(R.id.signed_up_events_listview);
        browseEvents = rootView.findViewById(R.id.browse_events_button);

        ArrayList<Event> checkedInEvents = new ArrayList<>();
        ArrayList<Event> signedUpEvents = new ArrayList<>();
        HomeCheckedInListAdapter homeCheckedInListAdapter = new HomeCheckedInListAdapter(getContext(), checkedInEvents);
        HomeSignedUpListAdapter homeSignedUpListAdapter = new HomeSignedUpListAdapter(getContext(), signedUpEvents);

        //if the attendee is not passed, we must get the attendee to display only the events they are in.
        if (getArguments() == null){

            FirebaseDB.getInstance().loginUser(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID), new FirebaseDB.GetAttendeeCallBack() {
                @Override
                public void onResult(Attendee attendee) {
                    FirebaseDB.getInstance().getEventsCheckedIn(attendee, checkedInEvents, homeCheckedInListAdapter);
                    FirebaseDB.getInstance().getAttendeeSignedUpEvents(attendee, signedUpEvents, homeSignedUpListAdapter);
                }
            });
        } else{
            Attendee attendee = (Attendee) getArguments().getSerializable("user");
            if (attendee == null){
                attendee = CurrentUser.getInstance().getCurrentUser();
            }
            FirebaseDB.getInstance().getEventsCheckedIn(attendee, checkedInEvents, homeCheckedInListAdapter);
            FirebaseDB.getInstance().getAttendeeSignedUpEvents(attendee, signedUpEvents, homeSignedUpListAdapter);
        }

        checkedIn.setAdapter(homeCheckedInListAdapter);
        signedUp.setAdapter(homeSignedUpListAdapter);

        checkedIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable("event", checkedInEvents.get(i));
                args.putSerializable("attendee", attendee);
                args.putSerializable("isCheckedIn", true);
                Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_eventDetailsFragment3, args);
            }
        });

        signedUp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable("event", signedUpEvents.get(i));
                args.putSerializable("attendee", attendee);
                args.putSerializable("isCheckedIn", false);
                Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_eventDetailsFragment3, args);
            }
        });

        browseEvents.setOnClickListener(v -> {
            Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_eventList3);
        });

        return rootView;
    }

    /**
     * Creates and returns a new instance of this fragment, optionally pre-populated with an
     * {@link Attendee} object through arguments. This method allows for re-creation of the
     * fragment's state or initialization with specific data.
     *
     * @param attendee The {@link Attendee} whose event participation will be displayed.
     * @return A new instance of {@link HomeFragment} with attendee data if provided.
     */
    public static HomeFragment newInstance(Attendee attendee){
        Bundle args = new Bundle();
        args.putSerializable("user", attendee);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

}