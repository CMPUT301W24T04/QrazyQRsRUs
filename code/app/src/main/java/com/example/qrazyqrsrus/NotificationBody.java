package com.example.qrazyqrsrus;

//we define the java object that represents the body of the notification that we use to communicate notification data to the server
//this idea was taken from Phillipp Lackner (https://www.youtube.com/@PhilippLackner)
//this was adapted from his video https://www.youtube.com/watch?v=q6TL2RyysV4&ab_channel=PhilippLackner, Accessed Mar. 23rd, 2024
/**
 * Represents the body of a notification to be sent to the server for dispatch. This class encapsulates
 * the necessary details required to craft a notification message, including the title, body, and associated event ID.
 * This structure is utilized for communication with notification services or servers, facilitating the delivery
 * of event-related notifications to users.
 *
 * Adapted from Philipp Lackner's tutorial on sending notifications with Firebase.
 */
public class NotificationBody {
    private String title;
    private String body;
    private String eventId;

    /**
     * Constructs a new NotificationBody instance with specified title, body, and event ID.
     *
     * @param title The title of the notification.
     * @param body The body text of the notification.
     * @param eventId The unique identifier of the event associated with this notification.
     */

    public NotificationBody(String title, String body, String eventId) {
        this.title = title;
        this.body = body;
        this.eventId = eventId;
    }

    /**
     * Retrieves the title of the notification.
     *
     * @return The title of the notification.
     */

    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the notification.
     *
     * @param title The new title of the notification.
     */

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the body text of the notification.
     *
     * @return The body text of the notification.
     */


    public String getBody() {
        return body;
    }

    /**
     * Sets the body text of the notification.
     *
     * @param body The new body text of the notification.
     */

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Retrieves the event ID associated with the notification.
     *
     * @return The event ID associated with the notification.
     */

    public String getEventId() {
        return eventId;
    }

    /**
     * Sets the event ID associated with the notification.
     *
     * @param eventId The new event ID to associate with the notification.
     */

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
