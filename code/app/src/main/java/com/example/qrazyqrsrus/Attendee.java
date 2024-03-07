package com.example.qrazyqrsrus;

import java.io.Serializable;

public class Attendee implements Serializable {
    private String id;
    private String documentId;
    private String name;
    private String email;
    private String profilePicturePath;
    private Boolean geolocationOn;

    public Attendee() {

    }
    // Constructor for a new attendee
    public Attendee(String id) {
        this.id = id;
    }

    // Constructor for attendee instance gotten from Firestore
    public Attendee(String id, String documentId, String name, String email, String profilePicturePath, Boolean geolocationOn) {
        this.id = id;
        this.documentId = documentId;
        this.name = name;
        this.email = email;
        this.profilePicturePath = profilePicturePath;
        this.geolocationOn = geolocationOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public Boolean getGeolocationOn() {
        return geolocationOn;
    }

    public void setGeolocationOn(Boolean geolocationOn) {
        this.geolocationOn = geolocationOn;
    }
}
