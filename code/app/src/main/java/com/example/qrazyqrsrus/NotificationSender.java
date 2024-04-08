package com.example.qrazyqrsrus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Manages the sending of notifications through a Firebase Cloud Messaging (FCM) API. This class utilizes Retrofit to
 * interact with a backend server that communicates with FCM to send push notifications. It supports both direct
 * messages to individual devices and broadcast messages to topics.
 *
 * Adapted from Philipp Lackner's tutorial on sending notifications with Firebase.
 */

public class NotificationSender implements Callback<MessageSentResponse> {
    //we use Retrofit to create our Java interface out of the HTTP Api defined on the backend
    //this definition was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
    //this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
    private final FcmApi api = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(FcmApi.class);
    private static NotificationSender instance = null;

    /**
     * Protected constructor to prevent direct instantiation from outside the class. Use {@link #getInstance()} to get an instance of this class.
     */

    protected NotificationSender(){

    }

    /**
     * Singleton pattern implementation to ensure only one instance of NotificationSender exists throughout the application lifecycle.
     *
     * @return The single instance of NotificationSender.
     */

    public static NotificationSender getInstance(){
        if (instance == null){
            instance = new NotificationSender();
        }
        return instance;
    }

    //this function was adapted from a function written by Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
    //it was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner Accessed on Mar. 23rd, 2024
    /**
     * This function sends the HTTP request to our API that will tell firebase to send a push notification
     * @param isBroadcast a boolean that represents whether this message should be broadcast or not
     * @param to a string that indicates the token of the individual if we are only sending a message to one person.
     *           this is only used when sending a milestone update to the organizer
     * @param topic a string that represents the topic name if we are sending a message to a group of people.
     *                    this is used when we make new announcements, we send the announcement to the group of users
     *                    signed up for the event, who are subscribed to the event under the topic labelled with the event document ID
     * @param eventName a string that represents the title of the notification
     * @param notificationText a string that represents the body of the notification
     */
    public void sendMessage(Boolean isBroadcast, String to, String topic, String eventName, String notificationText, String eventId){
        NotificationBody body = new NotificationBody(eventName, notificationText, eventId);

        SendMessageDto dto = new SendMessageDto(to, body, topic, eventId);

        try{
            if (isBroadcast){
                //we make an asynchronous HTTP request to the server to send a message to the topic
                api.broadcast(dto).enqueue(this);
            } else{
                //we make an asynchronous HTTP request to the server to send a message to a specific user
                api.sendMessage(dto).enqueue(this);
            }

        } catch (HttpException e){
            e.printStackTrace();
        }
    }

    /**
     * Callback method triggered when the Retrofit call to the server succeeds. This method is empty and can be customized
     * to handle response data or perform further actions upon successful notification delivery.
     *
     * @param call The executed Retrofit call.
     * @param response The response received from the server.
     */

    @Override
    public void onResponse(Call<MessageSentResponse> call, Response<MessageSentResponse> response) {
    }

    /**
     * Callback method triggered when the Retrofit call to the server fails. This method is empty and can be customized
     * to handle errors or perform cleanup actions upon failure.
     *
     * @param call The executed Retrofit call.
     * @param t The throwable that caused the failure.
     */

    @Override
    public void onFailure(Call<MessageSentResponse> call, Throwable t) {
    }
}
