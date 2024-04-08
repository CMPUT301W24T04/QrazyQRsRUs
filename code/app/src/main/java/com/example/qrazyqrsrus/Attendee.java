// Holds attributes of an attendee
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
    private long checkins;

    public Attendee() {

    }

    /**
     * Constructor for attendee with only id
     * @param id id of the device the user is using
     */
    // Constructor for a new attendee
    public Attendee(String id) {
        this.id = id;
        this.name = "Guest24";
        this.geolocationOn = false;
    }

    /**
     * Constructor for attendee with full information
     * @param id id of device user is using
     * @param documentId id of document of user in firestore
     * @param name name of user
     * @param email email of user
     * @param profilePicturePath (Relative) path of profile picture of user in Storage
     * @param geolocationOn Whether or not the user allow their geolocation to be tracked
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
    public Attendee(String id, String documentId, String name, String email, String profilePicturePath, Boolean geolocationOn, long checkins) {
        this.id = id;
        this.documentId = documentId;
        this.name = name;
        this.email = email;
        this.profilePicturePath = profilePicturePath;
        this.geolocationOn = geolocationOn;
        this.checkins = checkins;
    }

    /**
     * Constructor for attendee with limited information when their info is not full inputted
     * @param name name of user
     * @param documentId document id of the document storing the user's information
     * @param id deviceId of the device the user is using
     */
    public Attendee(String name, String documentId, String id, long checkins) {
        this.name = name;
        this.documentId = documentId;
        this.id = id;
        this.checkins = checkins;
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
        if (name == null || name.isEmpty())
        {
            this.name = "GuestT04";
        }
        else {
            this.name = name;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isEmpty())
        {
            this.email = null;
        }
        else {
            this.email = email;
        }
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

    public long getCheckins() {
        return checkins;
    }

    public void setCheckins(long checkins) {
        this.checkins = checkins;
    }
}
