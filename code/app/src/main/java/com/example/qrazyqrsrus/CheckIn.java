// This class holds the attributed for checked in attendees
package com.example.qrazyqrsrus;

public class CheckIn {
    private String attendeeDocId;
    private String documentId;
    private String eventDocId;
    private Double longitude;
    private Double latitude;
    private long numberOfCheckIns;

    public CheckIn(){

    }

    public CheckIn(String attendeeDocId, String eventDocId, Double longitude, Double latitude) {
        this.attendeeDocId = attendeeDocId;
        this.eventDocId = eventDocId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.numberOfCheckIns = 1;
    }

    public CheckIn(String attendeeDocId, String documentId, String eventDocId, Double longitude, Double latitude, long numberOfCheckIns) {
        this.attendeeDocId = attendeeDocId;
        this.documentId = documentId;
        this.eventDocId = eventDocId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.numberOfCheckIns = numberOfCheckIns;
    }

    public String getAttendeeDocId() {
        return attendeeDocId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getNumberOfCheckIns() {
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

    public void setNumberOfCheckIns(long numberOfCheckIns) {
        this.numberOfCheckIns = numberOfCheckIns;
    }
}
