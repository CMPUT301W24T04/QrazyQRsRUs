package com.example.qrazyqrsrus;

import java.io.Serializable;

/**
 * Attendee class represents attendees
 */
public class Attendee implements Serializable {
    private String id;
    private String documentId;
    private String name;
    private String email;
    private String profilePicturePath;
    private Boolean geolocationOn;
    private Boolean signedup; //added

    public Attendee() {

    }

    /**
     * Constructor for attendee with only id
     * @param id
     */
    // Constructor for a new attendee
    public Attendee(String id) {
        this.id = id;
    }

    /**
     * Constructor for attendee with full information
     * @param id
     * @param documentId
     * @param name
     * @param email
     * @param profilePicturePath
     * @param geolocationOn
     */
    // Constructor for attendee instance gotten from Firestore
    public Attendee(String id, String documentId, String name, String email, String profilePicturePath, Boolean geolocationOn) {
        this.id = id;
        this.documentId = documentId;
        this.name = name;
        this.email = email;
        this.profilePicturePath = profilePicturePath;
        this.geolocationOn = geolocationOn;
    }

    /**
     * Constructor for attendee with limited information when their info is not full inputted
     * @param noName
     * @param documentId
     * @param id
     */
    public Attendee(String noName, String documentId, String id) {
        this.name = noName;
        this.documentId = documentId;
        this.id = id;
    }

    public Attendee(String noName, String documentId, String id, boolean signedup) {
        this.name = noName;
        this.documentId = documentId;
        this.id = id;
        this.signedup = signedup;
    }

    /**
     * Getters and setters for attendee class
     * @return What the particular attendee attribute is to other classes, or changing it by a setter
     */
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
