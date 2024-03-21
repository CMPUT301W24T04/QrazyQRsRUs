package com.example.qrazyqrsrus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.qrazyqrsrus.databinding.ActivityMainBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;

    private NavController navController;
    private ArrayList<Event> signedUpEventList = new ArrayList<Event>();
    private HomeSignedUpListAdapter adapter;

    private String deviceId;

    Attendee[] user = new Attendee[1];
    private QRCodeScanHandler qrHandler = new QRCodeScanHandler(this, deviceId, new QRCodeScanHandler.ScanCompleteCallback() {
        //TODO: these callbacks only work on the first time a QR code is scanned after the app is launched

        @Override
        public void onPromoResult(Event matchingEvent) {
            ChangeFragment(EventDetailsFragment.newInstance(matchingEvent, user[0], false));

        }

        @Override
        public void onCheckInResult(Event event) {
            FirebaseDB.checkInAlreadyExists(event.getDocumentId(), user[0].getDocumentId(), new FirebaseDB.UniqueCheckInCallBack() {
                @Override
                public void onResult(boolean isUnique, CheckIn checkIn) {
                    if (isUnique) {
                        //if there is no existing checkIn with the attendee's document ID and the event's document ID we make a new one
                        CheckIn newCheckIn = new CheckIn(user[0].getDocumentId(), event.getDocumentId());
                        FirebaseDB.addCheckInToEvent(newCheckIn, event.getDocumentId());
                    } else{
                        //in this case the event should already have the checkIn in it's checkIn list
                        checkIn.incrementCheckIns();
                        FirebaseDB.updateCheckIn(checkIn);
                    }
                }
            });
            ChangeFragment(EventDetailsFragment.newInstance(event, user[0], true));

        }

        @Override
        public void onNoResult(@Nullable Event event, int errorNumber){
            if (event != null){
                ChangeFragment(EventDetailsFragment.newInstance(event, user[0], false));
                new ErrorDialog(R.string.not_signed_up_error).show(getSupportFragmentManager(), "QR Error Dialog");
            } else{
                new ErrorDialog(R.string.no_args).show(getSupportFragmentManager(), "QR Error Dialog");
            }



        }

    });

//    qrHandler =

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apparently this is not good practice, but if it works, it works.
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //CurrentUser.getInstance().initializeUser(deviceId);

        //Attendee[] user = new Attendee[1];
        FirebaseDB.loginUser(deviceId, new FirebaseDB.GetAttendeeCallBack() {
            @Override
            public void onResult(Attendee attendee) {
                user[0] = attendee;
                ChangeFragment(new HomeEventsFragment());
            }
        });



        if (deviceId == null) {
            Log.d("deviceId", "super badness");
            return;
        }

        // When the navigation bar is clicked
        binding.BottomNavView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();


            if (id == R.id.home) {
                ChangeFragment(new HomeEventsFragment());
            } else if (id == R.id.scan) {
                qrHandler.launch(user[0]);
            } else if (id == R.id.my_events) {
                ChangeFragment(new MyEventsFragment());
            } else if (id == R.id.profile) {
                //create a new instance of the ViewProfileFragment fragment, with the attendee that was obtained by logging in the user
                ChangeFragment(ViewProfileFragment.newInstance(user[0]));
            }

            return true;
        });


        if (user[0] == null) {
            Log.d("no user", "holy shit man");
            return;
        }

        FirebaseDB.getAttendeeSignedUpEvents(user[0],signedUpEventList, adapter);

        CollectionReference eventsRef = FirebaseDB.eventsCollection;

        // Add snapshot listener to listen for changes in the announcements array
        eventsRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                // Handle errors
                return;
            }

            // Loop through each document in the collection
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                // Get the event ID from the document
                String eventId = doc.getId();

                // Check if the user is signed up for this event
                for (Event userEvent : signedUpEventList) {
                    // Compare event IDs
                    if (userEvent.getDocumentId().equals(eventId)) {
                        // User is signed up for this event, now check for new announcements
                        List<String> announcements = (List<String>) doc.get("announcements");
                        if (announcements != null && !announcements.isEmpty()) {
                            // Send notification to the user for each new announcement
                            for (String announcement : announcements) {
                                Toast.makeText(this, "it works!", Toast.LENGTH_SHORT).show();
                                sendNotificationToUser(this, announcement);
                            }
                        }
                        break; // No need to continue checking other events
                    }
                }
            }
        });

        sendNotificationToUser(this, "does this even work?");



    }

    // Method to send notification to the user
    private void sendNotificationToUser(Context context, String announcement) {
        // Create an Intent to open the app when the notification is clicked
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Create the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New Announcement")
                .setContentText(announcement)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Get the NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if notification channels are supported (for Android Oreo and above)
        NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        // Show the notification
        notificationManager.notify(0, notificationBuilder.build());
    }

    /**
     * Use this method to display the fragment that is passed
     * as an argument
     *
     * @param fragment The fragment we want to display.
     */
    private void ChangeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
        //was getting java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState when using .commit()
        //fragmentTransaction.commitAllowingStateLoss();
    }


}