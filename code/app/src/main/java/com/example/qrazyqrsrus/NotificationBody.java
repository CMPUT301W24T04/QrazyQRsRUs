package com.example.qrazyqrsrus;

//we define the java object that represents the body of the notification that we use to communicate notification data to the server
//this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
public class NotificationBody {
    private String title;
    private String body;
    private String eventId;

    public NotificationBody(String title, String body, String eventId) {
        this.title = title;
        this.body = body;
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
