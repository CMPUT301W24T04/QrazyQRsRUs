package com.example.qrazyqrsrus;

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

import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Fragment to display the Announcement Edit section which
 * only the organizer can access. Enables them to send and delete
 * announcements.
 * @author Ikjyot - icansingh
 * @see AnnouncementsFragment
 * @version 1
 */
public class AnnouncementEditFragment extends Fragment {

    private EditText announcementEditText;
    private Button addButton;
    private ListView announcementListView;
    private Button backButton;
    private Button setTokenButton;
    private Button broadcastButton;
    private Button copyTokenButton;
    private EditText setTokenEditText;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> announcements;
    private String messageText;
    private String tokenToSendTo;
    //THIS MAY NOT BE CORRECT, specifically the create part
    private Event event;
    private FcmApi api = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(FcmApi.class);
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("Notification Permissions", "user accepted notification permissions");
                } else {
                    Log.e("Notification Permissions", "user denied notification permissions");
                }
            });

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

        createNotificationChannel();
        requestNotificationPermission();
        announcementEditText = rootView.findViewById(R.id.edit_announcement);
        addButton = rootView.findViewById(R.id.button_add);
        backButton = rootView.findViewById(R.id.button_back);
        announcementListView = rootView.findViewById(R.id.list_announcements);
        setTokenButton = rootView.findViewById(R.id.button_set_token);
        broadcastButton = rootView.findViewById(R.id.button_broadcast);
        setTokenEditText = rootView.findViewById(R.id.edit_token);
        copyTokenButton = rootView.findViewById(R.id.button_copy_token);

        announcements = event.getAnnouncements();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, announcements);
        announcementListView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(false);
                addAnnouncement(event);
                FirebaseDB.updateEvent(event); // Updates the database with new event
            }
        });

        announcementListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showDeleteConfirmationDialog(position, event);
                FirebaseDB.updateEvent(event);
                return true;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).popBackStack(); // Not sure how to do this (Used john's implementation from elsewhere
            }
        });

        setTokenButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tokenToSendTo = setTokenEditText.getText().toString();
            }
        });

        broadcastButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendMessage(true);
            }
        });

        copyTokenButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseDB.getToken();
            }
        });


        return rootView;
    }

    /**
     * This function launches a new activity where Android can request notification permissions if they have not yet been granted.
     */
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (!notificationManager.areNotificationsEnabled()){
                //THIS NEEDS TESTING, i don't know if it works, because my notifications were enabled already
                requestPermissionLauncher.launch("Can QrazyQRsRUs send you push notifications?");
            }
        }
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

    /**
     * This function sends the HTTP request to our API that will tell firebase to send a push notification
     * @param isBroadcast a boolean that represents whether this message should be broadcast or not
     */
    private void sendMessage(Boolean isBroadcast){
        String to;
        NotificationBody body = new NotificationBody(event.getName(), announcementEditText.getText().toString());
        if (!isBroadcast){
            to = tokenToSendTo;
        } else{
            to = null;
        }
        SendMessageDto dto = new SendMessageDto(to, body);

        try{
            if (isBroadcast){
                api.broadcast(dto);
            } else{
                api.sendMessage(dto);
            }

        } catch (HttpException e){
            e.printStackTrace();
        }

    }

    /**
     * This function registers a new Android Notification Channel where event announcements will be send to.
     * If the channel already exists, this does nothing.
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Event Announcements";
            String description = "Receive push notifications from event organizers";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("EVENTS", name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

