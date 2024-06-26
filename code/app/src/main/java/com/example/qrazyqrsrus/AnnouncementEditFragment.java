package com.example.qrazyqrsrus;
// This fragment allows announcements to be edited
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.compose.ui.text.AnnotatedString;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import android.Manifest;

/**
 * Fragment to display the Announcement Edit section which
 * only the organizer can access. Enables them to send and delete
 * announcements.
 * @author Ikjyot - icansingh
 * @see AnnouncementsFragment
 * @version 1
 */
public class AnnouncementEditFragment extends Fragment{

    private EditText announcementEditText;
    private Button addButton;
    private ListView announcementListView;
    private Button backButton;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> announcements;
    private Event event;
    private FirebaseDB firebaseDB;

    /**
     * Empty constructor for the fragment. Required for instantiation.
     */
    public AnnouncementEditFragment() {
        // Constructor
    }

    /**
     * Called to do initial creation of the fragment. This is where to perform any
     * one-time initializations.
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
     * the layout for the fragment and initializes the UI components involved in announcement
     * management.
     *
     * It retrieves the event details passed to it and sets up listeners for the UI components to handle
     * user interactions like adding and deleting announcements.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_announcement_edit, container, false);

        if (getArguments() == null){
            System.out.println("No Arguments provided");
            return null;
        }

        event = (Event) getArguments().get("event");
        assert event != null;

        if (this.firebaseDB == null) {
            this.firebaseDB = FirebaseDB.getInstance();
        }

        announcementEditText = rootView.findViewById(R.id.edit_announcement);
        addButton = rootView.findViewById(R.id.button_add);
        backButton = rootView.findViewById(R.id.button_back);
        announcementListView = rootView.findViewById(R.id.list_announcements);

        announcements = event.getAnnouncements();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, announcements);
        announcementListView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationSender.getInstance().sendMessage(true, null, event.getDocumentId(), event.getName(), announcementEditText.getText().toString(), event.getDocumentId());
                addAnnouncement(event);

            }
        });

        announcementListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showDeleteConfirmationDialog(position, event);

                return true;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).popBackStack(); // Not sure how to do this (Used john's implementation from elsewhere
            }
        });

        return rootView;
    }



    /**
     * Adds an announcement to the local list then updates the Event object's announcements
     * @param event the Event object with all the details of the event
     */
    private void addAnnouncement(Event event) {
        String newAnnouncement = announcementEditText.getText().toString().trim();

        if (!newAnnouncement.isEmpty()) {

            announcements.add(newAnnouncement);
            adapter.notifyDataSetChanged();
            event.setAnnouncements(announcements);
            firebaseDB.updateEvent(event); // Updates the database with new event
            announcementEditText.setText("");
            showToast("Announcement Added");

        }
    }

    /**
     * Displays a confirmation dialog asking organizer to confirm their deletion of announcement
     * @param position the position of the announcement in the list
     * @param event the Event object with all the details of the event
     */
    private void showDeleteConfirmationDialog(final int position, Event event) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Announcement")
                .setMessage("Are you sure you want to delete this announcement?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAnnouncement(position, event);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    /**
     * Deletes selected announcement from local list then updates the Event object's announcements
     * @param position the position of the announcement in the list
     * @param event the Event object with all the details of the event
     */
    private void deleteAnnouncement(final int position, Event event) {

        announcements.remove(position);
        adapter.notifyDataSetChanged();
        event.setAnnouncements(announcements);
        showToast("Announcement Deleted");
        firebaseDB.updateEvent(event);

    }

    /**
     * Generates a toast with a custom layout
     * @param message String that is to be displayed in the toast
     */
    private void showToast(String message) {
        Toast toast = new Toast(getContext());
        View toastView = getLayoutInflater().inflate(R.layout.toast_layout, null);
        TextView textView = toastView.findViewById(R.id.text_toast);
        textView.setText(message);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Factory method to create a new instance of this fragment using the provided {@link Event} object.
     * This method allows for the passing of event details to the fragment upon creation, enabling
     * targeted announcement management for that event.
     *
     * @param i The {@link Event} object containing details of the event for which announcements are to be managed.
     * @return A new instance of {@link AnnouncementEditFragment} with event details attached.
     */
    public static AnnouncementEditFragment newInstance(Event i){
        Bundle args = new Bundle();
        args.putSerializable("event", i);

        AnnouncementEditFragment fragment = new AnnouncementEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setFirebaseDB(FirebaseDB firebaseDB) {
        this.firebaseDB = firebaseDB;
    }
}

