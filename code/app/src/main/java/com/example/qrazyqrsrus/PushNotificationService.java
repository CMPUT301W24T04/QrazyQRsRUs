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
public class PushNotificationService extends FirebaseMessagingService {

    Context context = this;
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

        // Create an intent to open the activity containing the fragment
        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        // Include the event ID as an extra in the intent
        //intent.putExtra("eventId", eventId);

        // Set the intent action
        //intent.setAction(Intent.ACTION_VIEW);

        // Start the activity when the notification is clicked
        //PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);



    }
}
