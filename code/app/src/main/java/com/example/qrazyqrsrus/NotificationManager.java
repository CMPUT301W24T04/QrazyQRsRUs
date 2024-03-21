package com.example.qrazyqrsrus;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

    private Context context;
    private ArrayList<Event> signedUpEventList;

    public NotificationManager(Context context, ArrayList<Event> signedUpEventList){
        this.context = context;
        this.signedUpEventList = signedUpEventList;
    }

    public void startListeningForAnnouncements(){
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
                                sendNotificationToUser(announcement);
                            }
                        }
                        break; // No need to continue checking other events
                    }
                }
            }
        });
    }

    // Method to send notification to the user
    private void sendNotificationToUser(String announcement) {
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
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if notification channels are supported (for Android Oreo and above)
        NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", android.app.NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        // Show the notification
        notificationManager.notify(0, notificationBuilder.build());
    }
}
