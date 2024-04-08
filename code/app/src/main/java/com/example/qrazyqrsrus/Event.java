package com.example.qrazyqrsrus;
// Holds the attributes for an event created
import android.net.Uri;

import android.util.Log;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


public class Event implements Serializable {

    private String documentId;
    private String name;
    private String organizerId;
    private String location;
    private String details;
    private Integer maxAttendees;
    private String startDate;
    private String endDate;
    private Boolean geolocationOn;
    private String posterPath;
    private String qrCode;
    private String qrCodePromo;
    private ArrayList<String> announcements = new ArrayList<String>();
    private ArrayList<String> signUps= new ArrayList<String>();
    private ArrayList<String> checkIns= new ArrayList<String>();
    private String organizerToken;


    /**
     * Represents the event
     */
    // Default constructor
    public Event() {
    }
    // Constructor when Organizer creates the event
    public Event(String name, String organizerId, String details, String location, LocalDateTime startDate, LocalDateTime endDate, Integer maxAttendees) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.name = name;
        this.organizerId = organizerId;
        this.details = details;
        this.location = location;
        this.maxAttendees = maxAttendees;
        this.startDate = startDate.format(formatter);
        this.endDate = endDate.format(formatter);
        this.geolocationOn = true;
        this.announcements = new ArrayList<String>();
        this.signUps = new ArrayList<String>();
        this.checkIns = new ArrayList<String>();
        this.maxAttendees = maxAttendees;
    }

    // Constructor when getting retrieving from database
    public Event(String documentId, String name, String organizerId, String details,
                 String location, LocalDateTime startDate, LocalDateTime endDate,
                 Boolean geolocationOn, String posterPath, String qrCode,
                 String qrCodePromo, String organizerToken, ArrayList<String> announcements, ArrayList<String> signUps,
                 ArrayList<String> checkIns, Integer maxAttendees) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.documentId = documentId;
        this.name = name;
        this.organizerId = organizerId;
        this.details = details;
        this.location = location;
        this.maxAttendees = maxAttendees;
        this.startDate = startDate.format(formatter);
        this.endDate = endDate.format(formatter);
        this.geolocationOn = geolocationOn;
        this.posterPath = posterPath;
        this.qrCode = qrCode;
        this.qrCodePromo = qrCodePromo;
        this.organizerToken = organizerToken;
        this.announcements = announcements;
        this.signUps = signUps;
        this.checkIns = checkIns;
        this.maxAttendees = maxAttendees;
    }

    public Event(String documentId, String name, String organizerId, String details,
                 String location, String startDate, String endDate,
                 Boolean geolocationOn, String posterPath, String qrCode,
                 String qrCodePromo, String organizerToken, ArrayList<String> announcements, ArrayList<String> signUps,
                 ArrayList<String> checkIns, Integer maxAttendees) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.documentId = documentId;
        this.name = name;
        this.organizerId = organizerId;
        this.details = details;
        this.location = location;
        this.maxAttendees = maxAttendees;
        this.startDate = startDate;
        this.endDate = endDate;
        this.geolocationOn = geolocationOn;
        this.posterPath = posterPath;
        this.qrCode = qrCode;
        this.qrCodePromo = qrCodePromo;
        this.organizerToken = organizerToken;
        this.announcements = announcements;
        this.signUps = signUps;
        this.checkIns = checkIns;
    }

    /**
     * Retrieves the document ID of the event.
     *
     * @return A string representing the unique document ID of the event.
     */
    public String getDocumentId() { return documentId; }
    /**
     * Sets the document ID for the event.
     *
     * @param documentId A string representing the unique document ID to set for the event.
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    /**
     * Retrieves the name of the event.
     *
     * @return The name of the event.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the event. If the provided name is null or empty, defaults to "Common Event".
     *
     * @param eventName The name to set for the event.
     */
    public void setName(String eventName) {
        if (eventName == null || eventName.isEmpty()) {
            this.name = "Common Event";
        }
        else {
            this.name = eventName;
        }
    }
    /**
     * Retrieves the organizer's ID for the event.
     *
     * @return The organizer's ID as a string.
     */
    public String getOrganizerId() {
        return organizerId;
    }
    /**
     * Sets the organizer's ID for the event.
     *
     * @param organizerId The organizer's ID as a string.
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }
    /**
     * Retrieves the details or description of the event.
     *
     * @return The details of the event.
     */
    public String getDetails() {
        return details;
    }
    /**
     * Sets the details or description of the event. If the provided details are null or empty,
     * defaults to "No Description Available".
     *
     * @param details The details or description to set for the event.
     */
    public void setDetails(String details) {
        if (details == null || details.isEmpty()) {
            this.details = "No Description Available";
        }
        else {
            this.details = details;
        }
    }
    /**
     * Retrieves the location of the event.
     *
     * @return The location of the event.
     */
    public String getLocation() {
        return location;
    }
    /**
     * Sets the location of the event. If the provided location is null or empty, defaults to "N/A".
     *
     * @param location The location to set for the event.
     */
    public void setLocation(String location) {
        if (location == null || location.isEmpty()) {
            this.location = "N/A";
        }
        else {
            this.location = location;
        }
    }
    /**
     * Retrieves the start date of the event.
     *
     * @return The start date of the event in "yyyy-MM-dd HH:mm" format.
     */
    public String getStartDate() {
        return startDate;
    }
    /**
     * Retrieves the end date of the event.
     *
     * @return The end date of the event in "yyyy-MM-dd HH:mm" format.
     */
    public String getEndDate() {
        return endDate;
    }
    /**
     * Retrieves the status of geolocation for the event.
     *
     * @return A Boolean indicating if geolocation is enabled for the event.
     */
    public Boolean getGeolocationOn() {
        return geolocationOn;
    }
    /**
     * Sets the status of geolocation for the event.
     *
     * @param geolocationOn A Boolean indicating if geolocation should be enabled for the event.
     */
    public void setGeolocationOn(Boolean geolocationOn) {
        this.geolocationOn = geolocationOn;
    }
    /**
     * Retrieves the path of the poster image for the event.
     *
     * @return The file path or URL of the event's poster image.
     */
    public String getPosterPath() {
        return posterPath;
    }
    /**
     * Sets the path of the poster image for the event.
     *
     * @param posterPath The file path or URL of the event's poster image.
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    /**
     * Retrieves the QR code for the event.
     *
     * @return A string representing the QR code for the event.
     */
    public String getQrCode() {
        return qrCode;
    }
    /** Sets the qr code used to check in
     *
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
    /** Gets the promo QR code (as a string)
     *
     * @return String
     */
    public String getQrCodePromo() {
        return qrCodePromo;
    }
    /**
     * Sets the promotional QR code for the event.
     *
     * @param qrCodePromo A string representing the promotional QR code for the event.
     */
    public void setQrCodePromo(String qrCodePromo) {
        this.qrCodePromo = qrCodePromo;
    }
    /**
     * Retrieves the list of announcements associated with the event.
     *
     * @return An ArrayList of strings representing the announcements for the event.
     */
    public ArrayList<String> getAnnouncements() {
        return announcements;
    }
    /**
     * Sets the list of announcements for the event.
     *
     * @param announcements An ArrayList of strings representing the announcements for the event.
     */
    public void setAnnouncements(ArrayList<String> announcements) {
        this.announcements = announcements;
    }
    /**
     * Adds a new announcement to the list of announcements for the event.
     *
     * @param announcement A string representing the new announcement to add.
     */
    public void addAnnouncement(String announcement) {
        if (announcement != null && !announcement.isEmpty()) {
            this.announcements.add(announcement);
        }
    }
    /**
     * Retrieves the list of sign-ups for the event.
     *
     * @return An ArrayList of strings representing the sign-ups for the event.
     */
    public ArrayList<String> getSignUps() {
        return signUps;
    }
    /**
     * Sets the list of sign-ups for the event.
     *
     * @param signUps An ArrayList of strings representing the sign-ups for the event.
     */
    public void setSignUps(ArrayList<String> signUps) {
        this.signUps = signUps;
    }
    /**
     * Adds a new sign-up to the list of sign-ups for the event.
     *
     * @param signUp A string representing the new sign-up to add.
     */
    public void addSignUp(String signUp) {
        if (this.maxAttendees == null || (this.getAttendeeCount() < this.maxAttendees)){
            this.signUps.add(signUp);
            // We check the attendee count (in both checkins and signups) to make sure that we don't send more than 1 signup milestone
            if (this.getAttendeeCount() == 1) {
                NotificationSender.getInstance().sendMessage(false, this.organizerToken, null, "Milestone: " + this.name, "You've got your 1st signup!", this.getDocumentId());
            } else if (this.getAttendeeCount() == 10) {
                NotificationSender.getInstance().sendMessage(false, this.organizerToken, null, "Milestone: " + this.name, "You've got your 10th signup!", this.getDocumentId());
            } else if (this.getAttendeeCount() == 100) {
                NotificationSender.getInstance().sendMessage(false, this.organizerToken, null, "Milestone: " + this.name, "You've got your 100th signup!", this.getDocumentId());
            }
        }
    }

    /**
     * Deletes a sign-up from the list of sign-ups for the event.
     *
     * @param userId A string representing the ID of the user to delete from the sign-ups.
     * @return A boolean indicating if the sign-up was successfully deleted.
     */
    public boolean deleteSignUp(String userId) {
        if (signUps.contains(userId)){
            this.signUps.remove(userId);
            return true;
        }
        return false;
    }
    /**
     * Retrieves the list of check-ins for the event.
     *
     * @return An ArrayList of strings representing the check-ins for the event.
     */
    public ArrayList<String> getCheckIns() {
        return checkIns;
    }
    /**
     * Sets the list of check-ins for the event.
     *
     * @param checkIns An ArrayList of strings representing the check-ins for the event.
     */
    public void setCheckIns(ArrayList<String> checkIns) {
        this.checkIns = checkIns;
    }
    /**
     * Adds a new check-in to the list of check-ins for the event.
     *
     * @param checkIn A string representing the new check-in to add.
     */
    public void addCheckIn(String checkIn) {
        this.checkIns.add(checkIn);

        // Change this to notification when we've implemented notification
        if (checkIns.size() == 1) {
            NotificationSender.getInstance().sendMessage(false, this.organizerToken, null, "Milestone: " + this.name, "You've got your 1st check-in!", this.getDocumentId());
        } else if (checkIns.size() == 10) {
            NotificationSender.getInstance().sendMessage(false, this.organizerToken, null, "Milestone: " + this.name, "You've got your 10th check-in!", this.getDocumentId());
        } else if (checkIns.size() == 100) {
            NotificationSender.getInstance().sendMessage(false, this.organizerToken, null, "Milestone: " + this.name, "You've got your 100th check-in!", this.getDocumentId());
        }
    }

    /**
     * Deletes a check-in from the list of check-ins for the event.
     *
     * @param checkIn A string representing the check-in to delete.
     */
    public void deleteCheckIn(String checkIn) {
        this.checkIns.remove(checkIn);
    }

    /**
     * Retrieves the maximum number of attendees allowed for the event.
     *
     * @return An Integer representing the maximum number of attendees.
     */

    public Integer getMaxAttendees(){
        return this.maxAttendees;
    }

    /**
     * Sets the maximum number of attendees allowed for the event.
     *
     * @param maxAttendees An Integer representing the maximum number of attendees to set.
     */
    public void setMaxAttendees(Integer maxAttendees){
        this.maxAttendees = maxAttendees;
    }

    /**
     * Retrieves the current number of attendees for the event, including both sign-ups and check-ins.
     *
     * @return An Integer representing the current total number of attendees.
     */
    public Integer getAttendeeCount(){
        return (this.signUps == null ? 0 : this.signUps.size()) + (this.checkIns == null ? 0 : this.checkIns.size());
    }

    /**
     * Retrieves the Firebase Cloud Messaging token of the organizer.
     *
     * @return A string representing the FCM token of the organizer.
     */
    public String getOrganizerToken() {
        return organizerToken;
    }

    /**
     * Sets the Firebase Cloud Messaging token of the organizer.
     *
     * @param organizerToken A string representing the FCM token to set for the organizer.
     */
    public void setOrganizerToken(String organizerToken) {
        this.organizerToken = organizerToken;
    }

    /**
     * This constructor creates a new event using the Event Builder
     * @param builder the event builder we are building from
     */
    private Event(EventBuilder builder){
        this.documentId = null;
        this.name = builder.name;
        this.organizerId = builder.organizerId;
        this.details = builder.details;
        this.maxAttendees = builder.maxAttendees;
        this.location = builder.location;
        this.startDate = builder.startDate; //.format(formatter);
        this.endDate = builder.endDate; //.format(formatter);
        this.geolocationOn = builder.geolocationOn;
        this.posterPath = builder.posterPath;
        this.qrCode = builder.qrCode;
        this.qrCodePromo = builder.qrCodePromo;
        this.organizerToken = builder.organizerToken;
        this.announcements = new ArrayList<String>();
        this.signUps = new ArrayList<String>();
        this.checkIns = new ArrayList<String>();
    }

    public static class EventBuilder implements Serializable{
        private String name = null;
        private String organizerId = null;
        private String location = null;
        private String details = null;
        private Integer maxAttendees = null;
        private String startDate = null;
        private String endDate = null;
        private Boolean geolocationOn = null;
        private String posterPath = null;
        private String qrCode = null;
        private String qrCodePromo = null;
        private Uri uri = null;
        private String organizerToken = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrganizerId() {
            return organizerId;
        }

        public void setOrganizerId(String organizerId) {
            this.organizerId = organizerId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public Integer getMaxAttendees() {
            return maxAttendees;
        }

        public void setMaxAttendees(Integer maxAttendees) {
            this.maxAttendees = maxAttendees;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public Boolean getGeolocationOn() {
            return geolocationOn;
        }

        public void setGeolocationOn(Boolean geolocationOn) {
            this.geolocationOn = geolocationOn;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public String getQrCodePromo() {
            return qrCodePromo;
        }

        public void setQrCodePromo(String qrCodePromo) {
            this.qrCodePromo = qrCodePromo;
        }

        public Uri getUri() {
            return uri;
        }

        public void setUri(Uri uri) {
            this.uri = uri;
        }

        public String getOrganizerToken() {
            return organizerToken;
        }

        public void setOrganizerToken(String organizerToken) {
            this.organizerToken = organizerToken;
        }

        public Event build(){
            //return a new Event using the builder constructor
            return new Event(this);
        }
    }

}