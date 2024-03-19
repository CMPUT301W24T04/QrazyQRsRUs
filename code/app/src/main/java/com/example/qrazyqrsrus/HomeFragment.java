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

public class HomeFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    ListView checkedIn;
    ListView signedUp;
    private Attendee attendee;

    com.example.qrazyqrsrus.HomeCheckedInListAdapter checkedInListAdapter;
    com.example.qrazyqrsrus.HomeSignedUpListAdapter signedUpListAdapter;

    ArrayList<Event> checkedInEvents = new ArrayList<>();
    ArrayList<Event> signedUpEvents = new ArrayList<>();

    FloatingActionButton browseEvents;

    ImageButton adminButton;
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment


        ListView checkedIn = rootView.findViewById(R.id.checked_in_events_listview);
        ListView signedUp = rootView.findViewById(R.id.signed_up_events_listview);
        browseEvents = rootView.findViewById(R.id.browse_events_button);
        adminButton = rootView.findViewById(R.id.admin_button);

        ArrayList<Event> checkedInEvents = new ArrayList<>();
        ArrayList<Event> signedUpEvents = new ArrayList<>();
        HomeCheckedInListAdapter homeCheckedInListAdapter = new HomeCheckedInListAdapter(getContext(), checkedInEvents);
        HomeSignedUpListAdapter homeSignedUpListAdapter = new HomeSignedUpListAdapter(getContext(), signedUpEvents);

        //if the attendee is not passed, we must get the attendee to display only the events they are in.
        if (getArguments() == null){

            FirebaseDB.loginUser(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID), new FirebaseDB.GetAttendeeCallBack() {
                @Override
                public void onResult(Attendee attendee) {
                    FirebaseDB.getAttendeeCheckedInEvents(attendee, checkedInEvents, homeCheckedInListAdapter);
                    FirebaseDB.getAttendeeSignedUpEvents(attendee, signedUpEvents, homeSignedUpListAdapter);
                }
            });
        } else{
            Attendee attendee = (Attendee) getArguments().getSerializable("user");
            FirebaseDB.getAttendeeCheckedInEvents(attendee, checkedInEvents, homeCheckedInListAdapter);
            FirebaseDB.getAttendeeSignedUpEvents(attendee, signedUpEvents, homeSignedUpListAdapter);
        }

        checkedIn.setAdapter(homeCheckedInListAdapter);
        signedUp.setAdapter(homeSignedUpListAdapter);

        checkedIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable("event", checkedInEvents.get(i));
                args.putSerializable("attendee", attendee);
                Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_eventDetailsFragment3, args);
            }
        });

        signedUp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable("event", signedUpEvents.get(i));
                args.putSerializable("attendee", attendee);
//                NavHostFragment navHost = (NavHostgetView().findViewById(R.id.nav_graph_nav_host);
//                Navigation.findNavController(getView()).navigate(R.id.action_mainMenu_to_eventDetailsFragment, args);
                Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_eventDetailsFragment3, args);
            }
        });

        browseEvents.setOnClickListener(v -> {
            Navigation.findNavController(rootView).navigate(R.id.action_homeFragment_to_eventList3);
        });

        adminButton.setOnClickListener(v ->{
            PopupMenu popup = new PopupMenu(this.getContext(), v);
            popup.setOnMenuItemClickListener(this);
            popup.getMenuInflater().inflate(R.menu.admin_options_menu, popup.getMenu());
            popup.show();
        });



        return rootView;
    }

    public static HomeFragment newInstance(Attendee attendee){
        Bundle args = new Bundle();
        args.putSerializable("user", attendee);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.admin_mode_option){
            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_adminHomeFragment);
            return true;
        }
        return false;
    }
}