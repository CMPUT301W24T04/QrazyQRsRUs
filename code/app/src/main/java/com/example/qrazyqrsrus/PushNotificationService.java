package com.example.qrazyqrsrus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//this class is required by FirebaseMessaging to generate unique tokens for users
//this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024

/**
 * A service that extends {@link FirebaseMessagingService} to handle the creation and management
 * of push notifications for the app. It receives notifications sent from Firebase Cloud Messaging (FCM)
 * and acts upon them, such as generating unique tokens for users and handling incoming notification messages.
 * This service is crucial for implementing FCM push notifications within the app, allowing for real-time
 * event updates and alerts to be sent to users.
 */

public class PushNotificationService extends FirebaseMessagingService {

    Context context = this;

    /**
     * Called when a new token for the default Firebase project is generated. This is where you would send the token
     * to your app server to send notifications to this device.
     *
     * @param token The new token.
     */

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    /**
     * Called when a message is received. This method is invoked when the app is in the foreground or background.
     * It handles the received FCM message, extracts data such as the event ID, and constructs a notification with
     * a deep link that navigates to the relevant event details within the app.
     *
     * @param message Instance of {@link RemoteMessage} containing the message data as key/value pairs.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        // Extract event ID from the notification data
        String eventId = message.getData().get("eventId");
        Bundle args = new Bundle();

        if (eventId != null){
            Log.d("NotificationService", "Please tell me it works");
            try {
                FirebaseDB.getInstance().getEventById(eventId, new FirebaseDB.GetEventCallback() {
                    @Override
                    public Event onSuccess(Event event) {
                        args.putSerializable("event", event);
                        PendingIntent pendingIntent = new NavDeepLinkBuilder(getApplicationContext())
                                .setGraph(R.navigation.notification_nav_graph)
                                .setDestination(R.id.eventDetailsFragment4)         // THIS IS A POSSIBLE SPOT FOR AN ERROR, I AM NOT SURE WHICH NAV GRAPH TO USE AND THEREFORE WHICH DESTINATION
                                .setArguments(args)
                                .createPendingIntent();

                        // Build the notification
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "EVENTS")
                                .setContentTitle(message.getNotification().getTitle())
                                .setContentText(message.getNotification().getBody())
                                .setSmallIcon(R.drawable.dialog_background)
                                .setContentIntent(pendingIntent) // Set the PendingIntent
                                .setAutoCancel(true);

                        // Show the notification
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.notify(0, notificationBuilder.build());

                        return null;

                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.d("getEventById", "failure");
                    }
                }); // Await literally waits for the task to give a result before moving on
                // Use the event object here

            } catch (Exception e) {
                Log.d("bad exception when generating notification", e.getMessage());
            }
        }else {
            Log.d("NotificationService", "It doesn't:(");
        }

    }
}
