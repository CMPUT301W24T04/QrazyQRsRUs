package com.example.qrazyqrsrus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//this class is required by FirebaseMessaging to generate unique tokens for users
//this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
public class PushNotificationService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        //for further customization

        // Extract event ID from the notification data
        String eventId = message.getData().get("eventId");

        if (eventId != null){
            Log.d("NotificationService", "Please tell me it works");
        }else {
            Log.d("NotificationService", "It doesn't:(");
        }

        // Create an intent to open the activity containing the fragment
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        // Include the event ID as an extra in the intent
        intent.putExtra("eventId", eventId);

        // Set the intent action or use a custom action if needed
        intent.setAction(Intent.ACTION_VIEW);

        // Start the activity when the notification is clicked
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setSmallIcon(R.drawable.dialog_background)
                .setContentIntent(pendingIntent) // Set the PendingIntent
                .setAutoCancel(true); // Close the notification when clicked

        // Show the notification
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(0, notificationBuilder.build());

    }
}
