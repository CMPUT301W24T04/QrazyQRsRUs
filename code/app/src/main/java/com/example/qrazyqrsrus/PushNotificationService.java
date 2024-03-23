package com.example.qrazyqrsrus;
//https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner on February 29th, 2024

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //for security, firebase generates new tokens periodically.
        //for simplicity, we do not handle new token generation, as we will get the latest token upon launch
        //if we were, we'd update our firebase db for the user with the latest token
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        //for further customization
    }
}
