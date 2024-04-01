package com.example.qrazyqrsrus;

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
    }

    public Event(String documentId, String name, String organizerId, String details,
                 String location, String startDate, String endDate,
                 Boolean geolocationOn, String posterPath, String qrCode,
                 String qrCodePromo, String organizerToken, ArrayList<String> announcements, ArrayList<String> signUps,
                 ArrayList<String> checkIns) {
        this.documentId = documentId;
        this.name = name;
        this.organizerId = organizerId;
        this.details = details;
        this.location = location;
        this.startDate = startDate; //.format(formatter);
        this.endDate = endDate; //.format(formatter);
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
     * event constructor
     * @param eventName
     * @param eventDetails
     * @param eventLocation
     * @param eventDate
     */
    public Event(String eventName, String eventDetails, String eventLocation, LocalDateTime eventDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.name = eventName;
        this.details = eventDetails;
        this.location = eventLocation;
        this.startDate = eventDate.format(formatter);
    }

    /** get
     *
     * @return String
     */
    public String getDocumentId() { return documentId; }
    /** get
     *
     * @return String
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    /** get
     *
     * @return String
     */
    public String getName() {
        return name;
    }
    /** get
     *
     * @return String
     */
    public void setName(String eventName) {
        this.name = eventName;
    }
    /** get
     *
     * @return String
     */
    public String getOrganizerId() {
        return organizerId;
    }
    /** set
     *
     * @return String
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }
    /** get
     *
     * @return String
     */
    public String getDetails() {
        return details;
    }
    /** set
     *
     * @return String
     */
    public void setDetails(String details) {
        this.details = details;
    }
    /** get
     *
     * @return String
     */
    public String getLocation() {
        return location;
    }
    /** set
     *
     * @return String
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /** get
     *
     * @return String
     */
    public String getStartDate() {
        return startDate;
    }
    /** get
     *
     * @return String
     */
    public String getEndDate() {
        return endDate;
    }
    /** get
     *
     * @return String
     */
    public Boolean getGeolocationOn() {
        return geolocationOn;
    }
    /** set
     *
     * @return String
     */
    public void setGeolocationOn(Boolean geolocationOn) {
        this.geolocationOn = geolocationOn;
    }
    /** get
     *
     * @return String
     */
    public String getPosterPath() {
        return posterPath;
    }
    /** set
     *
     * @return String
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    /** get
     *
     * @return String
     */
    public String getQrCode() {
        return qrCode;
    }
    /** set
     *
     * @return String
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
    /** get
     *
     * @return String
     */
    public String getQrCodePromo() {
        return qrCodePromo;
    }
    /** set
     *
     * @return
     */
    public void setQrCodePromo(String qrCodePromo) {
        this.qrCodePromo = qrCodePromo;
    }
    /** get
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getAnnouncements() {
        return announcements;
    }
    /** set
     *
     * @return ArrayList<String>
     */
    public void setAnnouncements(ArrayList<String> announcements) {
        this.announcements = announcements;
    }
    /** adds announcement
     *
     * @return String
     */
    public void addAnnouncement(String announcement) {
        this.announcements.add(announcement);
    }
    /** get
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getSignUps() {
        return signUps;
    }
    /** set
     *
     * @return ArrayList<String>
     */
    public void setSignUps(ArrayList<String> signUps) {
        this.signUps = signUps;
    }
    /** adds user
     *
     * @return
     */
    public void addSignUp(String signUp) {
        if (this.maxAttendees == null || (this.getAttendeeCount() < this.maxAttendees)){
            this.signUps.add(signUp);
            // Change this to notification when we've implemented notification
            if (signUps.size() == 1) {
                NotificationSender.getInstance().sendMessage(false, this.organizerToken, null, "Milestone: " + this.name, "You've got your 1st signup!", this.getDocumentId());
            } else if (signUps.size() == 10) {
                NotificationSender.getInstance().sendMessage(false, this.organizerToken, null, "Milestone: " + this.name, "You've got your 10th signup!", this.getDocumentId());
            } else if (signUps.size() == 100) {
                NotificationSender.getInstance().sendMessage(false, this.organizerToken, null, "Milestone: " + this.name, "You've got your 100th signup!", this.getDocumentId());
            }
        }
    }

    /** removes user
     *
     * @return
     */
    public boolean deleteSignUp(String userId) {
        if (signUps.contains(userId)){
            this.signUps.remove(userId);
            return true;
        }
        return false;
    }
    /** get
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getCheckIns() {
        return checkIns;
    }
    /** get
     *
     * @return ArrayList<String>
     */
    public void setCheckIns(ArrayList<String> checkIns) {
        this.checkIns = checkIns;
    }
    /** adds checkin
     *
     * @return
     */
    public void addCheckIn(String checkIn) {
        if (this.maxAttendees == null || this.getAttendeeCount() < this.maxAttendees) {
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
    }

    /** removes checkin
     *
     * @return
     */
    public void deleteCheckIn(String checkIn) {
        this.checkIns.remove(checkIn);
    }

    /** get
     *
     * @return integer of maximum number of attendees this event can have
     */

    public Integer getMaxAttendees(){
        return this.maxAttendees;
    }

    public void setMaxAttendees(Integer maxAttendees){
        this.maxAttendees = maxAttendees;
    }

    public Integer getAttendeeCount(){
        return this.signUps.size() + this.checkIns.size();
    }
    /**
     * checkes if user is checked in or signed up
     * @param userDocumentId
     * @param event
     * @return Boolean
     */


//    public static Boolean hasCheckedInOrSignedUp(String userDocumentId, Event event) {
//        if (event.getSignUps().contains(userDocumentId)) {
//            return true;
//        }
//        ArrayList<Attendee> tempList = new ArrayList<>();
//        ArrayList<String> tempList2 = new ArrayList<>();
//        FirebaseDB.getEventCheckedIn(event, tempList, attendeeListAdapter);
//        for (Attendee attendee : tempList) {
//            tempList2.add(attendee.getDocumentId());
//        }
//        if (tempList2.contains(userDocumentId)) {
//            return true;
//        }
//        return false;
//    }

    /** get
     *
     * @return string of the organizers FCM token
     */
    public String getOrganizerToken() {
        return organizerToken;
    }

    /** set
     *
     *
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