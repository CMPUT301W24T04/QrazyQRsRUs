package com.qrazyqrsrus.notifications;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.util.HashMap;
import java.util.Map;


//we define the java data transfer object that will represent the data we pass between the server to encode the notification
//this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
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

    //this function is used to convert a data transfer obejct into a Message object used by the firebase admin sdk to send push notifications
    //this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner), and converted to Java code
    //this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
    public Message toMessage(){
        Message.Builder messageBuilder = Message.builder()
                .setNotification(
                    Notification.builder()
                            .setTitle(notification.getTitle())
                            .setBody(notification.getBody())
                            .build()
                );

        // Include event ID in the data payload
        Map<String, String> data = new HashMap<>();
        data.put("eventId", eventId);
        messageBuilder.putAllData(data);

        if (to == null){
            messageBuilder.setTopic(topic);
        } else{
            messageBuilder.setToken(to);
        }

        return messageBuilder.build();

    }
}
