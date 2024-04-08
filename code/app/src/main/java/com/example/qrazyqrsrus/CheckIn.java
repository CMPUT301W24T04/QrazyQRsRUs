// This class holds the attributed for checked in attendees
package com.example.qrazyqrsrus;

import java.util.Map;

/**
 * Represents a check-in record for an attendee at an event. This class captures details about
 * the check-in process, including the attendee's document ID, the event document ID, geographical
 * coordinates of the check-in, and the total number of check-ins by the attendee.
 */
public class CheckIn {
    private String attendeeDocId;
    private String documentId;
    private String eventDocId;
    private Double longitude;
    private Double latitude;
    private long numberOfCheckIns;

    /**
     * Default constructor for creating a new CheckIn instance without setting any initial values.
     */
    public CheckIn(){

    }

    /**
     * Constructs a new CheckIn instance for an attendee check-in without geographical coordinates.
     *
     * @param attendeeDocId    The document ID of the attendee.
     * @param documentId       The unique identifier for this check-in record.
     * @param eventDocId       The document ID of the event where the check-in occurs.
     * @param numberOfCheckIns The total number of times the attendee has checked in.
     */
    public CheckIn(String attendeeDocId, String documentId, String eventDocId, long numberOfCheckIns) {
        this.attendeeDocId = attendeeDocId;
        this.documentId = documentId;
        this.eventDocId = eventDocId;
        this.numberOfCheckIns = numberOfCheckIns;
    }

    /**
     * Constructs a new CheckIn instance with geographical coordinates for the check-in location.
     *
     * @param attendeeDocId The document ID of the attendee.
     * @param eventDocId    The document ID of the event where the check-in occurs.
     * @param longitude     The longitude of the check-in location.
     * @param latitude      The latitude of the check-in location.
     */
    public CheckIn(String attendeeDocId, String eventDocId, Double longitude, Double latitude) {
        this.attendeeDocId = attendeeDocId;
        this.eventDocId = eventDocId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.numberOfCheckIns = 1;
    }

    /**
     * Constructs a new CheckIn instance including both geographical coordinates and a specific
     * number of check-ins.
     *
     * @param attendeeDocId    The document ID of the attendee.
     * @param documentId       The unique identifier for this check-in record.
     * @param eventDocId       The document ID of the event where the check-in occurs.
     * @param longitude        The longitude of the check-in location.
     * @param latitude         The latitude of the check-in location.
     * @param numberOfCheckIns The total number of times the attendee has checked in.
     */
    public CheckIn(String attendeeDocId, String documentId, String eventDocId, Double longitude, Double latitude, long numberOfCheckIns) {
        this.attendeeDocId = attendeeDocId;
        this.documentId = documentId;
        this.eventDocId = eventDocId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.numberOfCheckIns = numberOfCheckIns;
    }

    /**
     * Gets the document ID of the attendee associated with this check-in.
     *
     * @return The document ID of the attendee.
     */
    public String getAttendeeDocId() {
        return attendeeDocId;
    }

    /**
     * Gets the longitude of the check-in location.
     *
     * @return The longitude value.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the check-in location.
     *
     * @param longitude The new longitude value.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the latitude of the check-in location.
     *
     * @return The latitude value.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the check-in location.
     *
     * @param latitude The new latitude value.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the total number of check-ins performed by the attendee.
     *
     * @return The total number of check-ins.
     */
    public long getNumberOfCheckIns() {
        return numberOfCheckIns;
    }

    /**
     * Increments the number of check-ins by one. This method should be called each time an attendee
     * checks in to an event.
     */
    public void incrementCheckIn() {
        this.numberOfCheckIns++;
    }

    /**
     * Gets the unique document ID for this check-in record.
     *
     * @return The document ID of the check-in.
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * Gets the document ID of the event associated with this check-in.
     *
     * @return The document ID of the event.
     */
    public String getEventDocId() {
        return eventDocId;
    }

    /**
     * Sets the unique document ID for this check-in record.
     *
     * @param documentId The new document ID for the check-in.
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * Sets the document ID of the attendee associated with this check-in.
     *
     * @param attendeeDocId The new document ID of the attendee.
     */
    public void setAttendeeDocId(String attendeeDocId) {
        this.attendeeDocId = attendeeDocId;
    }


    /**
     * Sets the document ID of the event associated with this check-in.
     *
     * @param eventDocId The new document ID of the event.
     */
    public void setEventDocId(String eventDocId) {
        this.eventDocId = eventDocId;
    }

    /**
     * Sets the total number of check-ins performed by the attendee.
     *
     * @param numberOfCheckIns The new total number of check-ins.
     */
    public void setNumberOfCheckIns(long numberOfCheckIns) {
        this.numberOfCheckIns = numberOfCheckIns;
    }
}
