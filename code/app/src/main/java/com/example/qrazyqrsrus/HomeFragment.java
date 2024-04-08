package com.example.qrazyqrsrus;

// This fragment allows the user to browse events
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private FirebaseDB firebaseDB;
    private Attendee[] attendee;

    HomeCheckedInListAdapter homeCheckedInListAdapter;
    HomeSignedUpListAdapter homeSignedUpListAdapter;

    ArrayList<Event> checkedInEvents = new ArrayList<>();
    ArrayList<Event> signedUpEvents = new ArrayList<>();

    FloatingActionButton browseEvents;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Saves the bundle passed to the view when view is created
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Display the events the user to checked in and signed up for
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return rootView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment

        if (this.firebaseDB == null) {
            firebaseDB = FirebaseDB.getInstance();
        }
        checkedIn = rootView.findViewById(R.id.checked_in_events_listview);
        signedUp = rootView.findViewById(R.id.signed_up_events_listview);
        browseEvents = rootView.findViewById(R.id.browse_events_button);

        checkedInEvents = new ArrayList<>();
        signedUpEvents = new ArrayList<>();
        homeCheckedInListAdapter = new HomeCheckedInListAdapter(getContext(), checkedInEvents);
        homeSignedUpListAdapter = new HomeSignedUpListAdapter(getContext(), signedUpEvents);

        //if the attendee is not passed, we must get the attendee to display only the events they are in.
        if (getArguments() == null){

            firebaseDB.loginUser(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID), new FirebaseDB.GetAttendeeCallBack() {
                @Override
                public void onResult(Attendee attendee) {
                    firebaseDB.getEventsCheckedIn(attendee, checkedInEvents, homeCheckedInListAdapter);
                    firebaseDB.getAttendeeSignedUpEvents(attendee, signedUpEvents, homeSignedUpListAdapter);
                }

                @Override
                public void onNoResult() {
                    new ErrorDialog(R.string.login_error).show(getActivity().getSupportFragmentManager(), "Error Dialog");
                }
            });
        } else{
            Attendee attendee = (Attendee) getArguments().getSerializable("user");
            if (attendee == null){

                FirebaseDB.getInstance().loginUser(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID), new FirebaseDB.GetAttendeeCallBack() {
                    @Override
                    public void onResult(Attendee attendee) {
                        HomeFragment.this.attendee[0] = attendee;
                    }

                    @Override
                    public void onNoResult() {

                    }
                });
            } else{
                this.attendee[0] = attendee;
            }
            firebaseDB.getEventsCheckedIn(this.attendee[0], checkedInEvents, homeCheckedInListAdapter);
            firebaseDB.getAttendeeSignedUpEvents(this.attendee[0], signedUpEvents, homeSignedUpListAdapter);
        }

        checkedIn.setAdapter(homeCheckedInListAdapter);
        signedUp.setAdapter(homeSignedUpListAdapter);

        checkedIn.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle args = new Bundle();
            args.putSerializable("event", checkedInEvents.get(i));
            args.putSerializable("attendee", attendee);
            args.putSerializable("isCheckedIn", true);
            Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_eventDetailsFragment3, args);
        });

        signedUp.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle args = new Bundle();
            args.putSerializable("event", signedUpEvents.get(i));
            args.putSerializable("attendee", attendee);
            args.putSerializable("isCheckedIn", false);
            Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_eventDetailsFragment3, args);
        });

        browseEvents.setOnClickListener(v -> Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_eventList3));

        return rootView;
    }
    public void setFirebaseDB(FirebaseDB firebaseDB) {
        this.firebaseDB = firebaseDB;
    }

    /**
     * Puts the attendee in a bundle to be used
     * @return fragment
     */

    public static HomeFragment newInstance(Attendee attendee){
        Bundle args = new Bundle();
        args.putSerializable("user", attendee);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

}