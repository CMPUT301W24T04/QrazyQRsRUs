package com.example.qrazyqrsrus;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class NotificationSender implements Callback<MessageSentResponse> {
    private FcmApi api = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(FcmApi.class);
    private static NotificationSender instance = null;

    protected NotificationSender(){

    }

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

    @Override
    public void onResponse(Call<MessageSentResponse> call, Response<MessageSentResponse> response) {
    }

    @Override
    public void onFailure(Call<MessageSentResponse> call, Throwable t) {
    }
}
