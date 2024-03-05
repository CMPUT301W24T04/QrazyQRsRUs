package com.example.qrazyqrsrus;
import java.io.Serializable;

public class Event implements Serializable {
    private String eventName;
    private String location;
    private String date;
    private String details;

    // Default constructor
    public Event() {
    }

    public Event(String eventName, String location, String date, String details) {
        this.eventName = eventName;
        this.location = location;
        this.date = date;
        this.details = details;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}