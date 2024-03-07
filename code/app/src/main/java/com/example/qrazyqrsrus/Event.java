package com.example.qrazyqrsrus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;


public class Event implements Serializable {

    private String documentId;
    private String name;
    private String organizerId;
    private String location;
    private String details;
    private String startDate;
    private String endDate;
    private Boolean geolocationOn;
    private String posterPath;
    private String qrCodePath;
    private String qrCodePromoPath;
    private ArrayList<String> announcements;
    private ArrayList<String> signUps;
    private ArrayList<String> checkIns;

    // Default constructor

    /**
     * Holds the event
     * inputs: String name, String organizerId, String details, String location, LocalDateTime startDate, LocalDateTime endDate
     * outputs: the getters and setters
     */
    public Event() {
    }
    // Constructor when Organizer creates the event
    public Event(String name, String organizerId, String details, String location, String startDate, String endDate) {
        this.documentId = "0";
        this.name = name;
        this.organizerId = organizerId;
        this.details = details;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.geolocationOn = true;
        this.announcements = new ArrayList<String>();
        this.signUps = new ArrayList<String>();
        this.checkIns = new ArrayList<String>();
    }

    /**
     *
     * @param documentId
     * @param name
     * @param organizerId
     * @param details
     * @param location
     * @param startDate
     * @param endDate
     * @param geolocationOn
     * @param posterPath
     * @param qrCodePath
     * @param qrCodePromoPath
     * @param announcements
     * @param signUps
     * @param checkIns
     */
    // Constructor when getting retrieving from database
    public Event(String documentId, String name, String organizerId, String details,
                 String location, String startDate, String endDate,
                 Boolean geolocationOn, String posterPath, String qrCodePath,
                 String qrCodePromoPath, ArrayList<String> announcements, ArrayList<String> signUps,
                 ArrayList<String> checkIns) {
        this.documentId = documentId;
        this.name = name;
        this.organizerId = organizerId;
        this.details = details;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.geolocationOn = geolocationOn;
        this.posterPath = posterPath;
        this.qrCodePath = qrCodePath;
        this.qrCodePromoPath = qrCodePromoPath;
        this.announcements = announcements;
        this.signUps = signUps;
        this.checkIns = checkIns;
    }

    /**
     * gets document id
     * @return documentId
     */
    public String getDocumentId() { return documentId; }
    /**
     *
     * @return documentId
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String eventName) {
        this.name = eventName;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getQrCodePath() {
        return qrCodePath;
    }

    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }

    public String getQrCodePromoPath() {
        return qrCodePromoPath;
    }

    public void setQrCodePromoPath(String qrCodePromoPath) {
        this.qrCodePromoPath = qrCodePromoPath;
    }

    public ArrayList<String> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(ArrayList<String> announcements) {
        this.announcements = announcements;
    }

    public void addAnnouncement(String announcement) {
        this.announcements.add(announcement);
    }

    public ArrayList<String> getSignUps() {
        return signUps;
    }

    public void setSignUps(ArrayList<String> signUps) {
        this.signUps = signUps;
    }

    public void addSignUp(String signUp) {
        this.signUps.add(signUp);
    }

    public ArrayList<String> getCheckIns() {
        return checkIns;
    }

    public void setCheckIns(ArrayList<String> checkIns) {
        this.checkIns = checkIns;
    }

    public void addCheckIn(String checkIn) {
        this.checkIns.add(checkIn);
    }
}
//package com.example.qrazyqrsrus;
//import java.io.Serializable;
//
//public class Event implements Serializable {
//    private String eventName;
//    private String location;
//    private String date;
//    private String details;
//
//    // Default constructor
//    public Event() {
//    }
//
//    public Event(String eventName, String location, String date, String details) {
//        this.eventName = eventName;
//        this.location = location;
//        this.date = date;
//        this.details = details;
//    }
//
//    public String getEventName() {
//        return eventName;
//    }
//
//    public void setEventName(String eventName) {
//        this.eventName = eventName;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public String getDetails() {
//        return details;
//    }
//
//    public void setDetails(String details) {
//        this.details = details;
//    }
//
//}