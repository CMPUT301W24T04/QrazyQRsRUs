package com.example.qrazyqrsrus;

import com.google.gson.Gson;

//we define the java data transfer object that will represent the data we pass between the server to encode the notification
//this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024

/**
 * Represents a data transfer object (DTO) for sending messages through the server to Firebase Cloud Messaging (FCM).
 * This DTO includes fields for specifying the recipient(s) of the message, the content of the notification, and
 * any relevant topic or event ID. This class is utilized to package and send notification data to the server,
 * which then communicates with FCM to deliver push notifications to users. The structure and implementation
 * were inspired by instructional content by Phillipp Lackner.
 */

public class SendMessageDto {
    private String to;
    private NotificationBody notification;
    private String topic;
    private String eventId;

    public SendMessageDto(String to, NotificationBody notification, String topic, String eventId) {
        this.to = to;
        this.notification = notification;
        this.topic = topic;
        this.eventId = eventId;
    }

    /**
     * Gets the recipient's token. This token is unique to each device and is used to direct the message
     * to a specific user or device.
     *
     * @return A string representing the recipient's token.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the recipient's token. This token is unique to each device and is used to direct the message
     * to a specific user or device.
     *
     * @param to A string representing the recipient's token.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the notification body. The notification body contains the title and message body of the notification.
     *
     * @return An instance of {@link NotificationBody} containing the notification's details.
     */
    public NotificationBody getNotification() {
        return notification;
    }

    /**
     * Sets the notification body. The notification body contains the title and message body of the notification.
     *
     * @param notification An instance of {@link NotificationBody} to be sent as part of the notification.
     */
    public void setNotification(NotificationBody notification) {
        this.notification = notification;
    }

    /**
     * Gets the topic name. The topic name is used when sending messages to groups of users subscribed to a particular topic.
     *
     * @return A string representing the topic name.
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the topic name. The topic name is used when sending messages to groups of users subscribed to a particular topic.
     *
     * @param topic A string representing the topic name.
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Gets the event ID. The event ID can be used to identify the specific event associated with the notification.
     *
     * @return A string representing the event ID.
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets the event ID. The event ID can be used to identify the specific event associated with the notification.
     *
     * @param eventId A string representing the event ID.
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}
