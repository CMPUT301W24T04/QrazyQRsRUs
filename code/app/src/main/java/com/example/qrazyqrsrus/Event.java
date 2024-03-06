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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean geolocationOn;
    private String posterPath;
    private String qrCode;
    private String qrCodePromo;
    private ArrayList<String> announcements;
    private ArrayList<String> signUps;
    private ArrayList<String> checkIns;

    // Default constructor
    public Event() {
    }
    // Constructor when Organizer creates the event
    public Event(String name, String organizerId, String details, String location, LocalDateTime startDate, LocalDateTime endDate) {
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
    // Constructor when getting retrieving from database
    public Event(String documentId, String name, String organizerId, String details,
                 String location, LocalDateTime startDate, LocalDateTime endDate,
                 Boolean geolocationOn, String posterPath, String qrCode,
                 String qrCodePromo, ArrayList<String> announcements, ArrayList<String> signUps,
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
        this.qrCode = qrCode;
        this.qrCodePromo = qrCodePromo;
        this.announcements = announcements;
        this.signUps = signUps;
        this.checkIns = checkIns;
    }

    public String getDocumentId() { return documentId; }

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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
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

    public void deleteSignUp(String userId) {
        this.signUps.remove(userId);
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

    public void deleteCheckIn(String userId) {
        this.checkIns.remove(userId);
    }
}