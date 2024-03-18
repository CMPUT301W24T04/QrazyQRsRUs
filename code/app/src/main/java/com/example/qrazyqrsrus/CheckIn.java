package com.example.qrazyqrsrus;

import java.util.Map;

public class CheckIn {
    private String attendeeDocId;
    private String documentId;
    private String eventDocId;
    private Map<String, Object> location;
    private Integer numberOfCheckIns;

    public CheckIn() {

    }
    public CheckIn(String attendeeDocId, String eventDocId) {
        this.attendeeDocId = attendeeDocId;
        this.eventDocId = eventDocId;
        this.numberOfCheckIns = 1;
    }

    public CheckIn(String attendeeDocId, String documentId, String eventDocId, Map<String, Object> location, Integer numberOfCheckIns) {
        this.attendeeDocId = attendeeDocId;
        this.documentId = documentId;
        this.eventDocId = eventDocId;
        this.location = location;
        this.numberOfCheckIns = numberOfCheckIns;
    }

    public String getAttendeeDocId() {
        return attendeeDocId;
    }

    public Map<String, Object> getLocation() {
        return location;
    }

    public Integer getNumberOfCheckIns() {
        return numberOfCheckIns;
    }

    public void incrementCheckIn() {
        this.numberOfCheckIns++;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getEventDocId() {
        return eventDocId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setAttendeeDocId(String attendeeDocId) {
        this.attendeeDocId = attendeeDocId;
    }

    public void setEventDocId(String eventDocId) {
        this.eventDocId = eventDocId;
    }

    public void setLocation(Map<String, Object> location) {
        this.location = location;
    }

    public void setNumberOfCheckIns(Integer numberOfCheckIns) {
        this.numberOfCheckIns = numberOfCheckIns;
    }

    public void incrementCheckIns(){
        this.numberOfCheckIns += 1;
    }
}
