package com.example.qrazyqrsrus;
// This fragment allows announcements to be edited

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.ArrayList;

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
    private Button setTokenButton;
    private Button broadcastButton;
    private Button copyTokenButton;
    private EditText setTokenEditText;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> announcements;
    private String messageText;
    private String tokenToSendTo;
    private Event event;

    //TODO: find out if we can delete

    public AnnouncementEditFragment() {
        // Constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        announcementEditText = rootView.findViewById(R.id.edit_announcement);
        Button addButton = rootView.findViewById(R.id.button_add);
        Button backButton = rootView.findViewById(R.id.button_back);
        ListView announcementListView = rootView.findViewById(R.id.list_announcements);

        announcements = event.getAnnouncements();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, announcements);
        announcementListView.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            NotificationSender.getInstance().sendMessage(true, null, event.getDocumentId(), event.getName(), announcementEditText.getText().toString(), event.getDocumentId());
            addAnnouncement(event);

        });

        announcementListView.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteConfirmationDialog(position, event);

            return true;
        });

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(rootView).popBackStack(); // Not sure how to do this (Used john's implementation from elsewhere
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
            FirebaseDB.getInstance().updateEvent(event); // Updates the database with new event
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
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteAnnouncement(position, event))
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
        FirebaseDB.getInstance().updateEvent(event);

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

    public static AnnouncementEditFragment newInstance(Event i){
        Bundle args = new Bundle();
        args.putSerializable("event", i);

        AnnouncementEditFragment fragment = new AnnouncementEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //TODO: find out if we can delete

}

