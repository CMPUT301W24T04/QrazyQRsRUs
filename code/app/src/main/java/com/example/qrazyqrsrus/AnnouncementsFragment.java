package com.example.qrazyqrsrus;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display the Announcements section which users
 * can access through events. Enables them to view announcements
 * sent by the organizers.
 * @author Ikjyot - icansingh
 * @see AnnouncementEditFragment
 * @version 1
 */
public class AnnouncementsFragment extends Fragment {

    private ListView announcementListView;
    private ArrayList<String> announcements;
    private ArrayAdapter<String> adapter;
    private Button backButton;

    /**
     * Constructor for {@link AnnouncementsFragment}.
     * Initializes a new instance of the announcements fragment. This is the default constructor required by Android for fragments.
     */
    public AnnouncementsFragment() {
        // Constructor
    }

    /**
     * Called to do initial creation of the fragment. This method performs any one-time
     * initialization operations such as setting up the fragment's configuration.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this
     *                           is the state. This value may be null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view. This method inflates
     * the layout for the fragment, initializes the UI components, and sets up event listeners.
     * It also retrieves and displays the announcements for the event specified in the fragment's arguments.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to. The fragment
     *                           should not add the view itself, but this can be used to generate the LayoutParams
     *                           of the view.
     * @param savedInstanceState If non-null, the fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null if no UI could be created.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_announcements, container, false);

        if (getArguments() == null){
            System.out.println("No Arguments provided");
            return null;
        }

        Event event = (Event) getArguments().get("event");
        assert event != null;

        announcementListView = rootView.findViewById(R.id.list_announcements);
        backButton = rootView.findViewById(R.id.button_back);

        announcements = event.getAnnouncements();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, announcements);
        announcementListView.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).popBackStack(); // Not sure how to do this (Used john's implementation from elsewhere
            }
        });

        return rootView;
    }

    /**
     * Factory method to create a new instance of {@link AnnouncementsFragment}, providing the event
     * details as an argument. This allows the fragment to be initialized with specific information
     * about the event whose announcements are to be displayed.
     *
     * @param event The {@link Event} object containing details about the event.
     * @return A new instance of {@link AnnouncementsFragment} with event details as arguments.
     */
    public static AnnouncementsFragment newInstance(Event i){
        Bundle args = new Bundle();
        args.putSerializable("event", i);

        AnnouncementsFragment fragment = new AnnouncementsFragment();
        fragment.setArguments(args);
        return fragment;
    }

}

