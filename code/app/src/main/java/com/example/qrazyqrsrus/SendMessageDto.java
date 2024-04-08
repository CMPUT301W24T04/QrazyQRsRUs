package com.example.qrazyqrsrus;

import com.google.gson.Gson;

//we define the java data transfer object that will represent the data we pass between the server to encode the notification
//this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
public class SendMessageDto {
    private String to;
    private NotificationBody notification;
    private String topic;
    private String eventId;

    /**
     * Passes the notification between the server and the app
     * @param to
     * @param notification
     * @param topic
     * @param eventId
     */
    public SendMessageDto(String to, NotificationBody notification, String topic, String eventId) {
        this.to = to;
        this.notification = notification;
        this.topic = topic;
        this.eventId = eventId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationBody getNotification() {
        return notification;
    }

    public void setNotification(NotificationBody notification) {
        this.notification = notification;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}
