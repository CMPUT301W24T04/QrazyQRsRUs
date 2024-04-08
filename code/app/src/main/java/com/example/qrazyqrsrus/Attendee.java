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
     * @param id
     */
    // Constructor for a new attendee
    public Attendee(String id) {
        this.id = id;
        this.name = "Guest24";
        this.geolocationOn = false;
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
     * @param name
     * @param documentId
     * @param id
     */
    public Attendee(String name, String documentId, String id, long checkins) {
        this.name = name;
        this.documentId = documentId;
        this.id = id;
        this.checkins = checkins;
    }

    /**
     * Retrieves the unique identifier of the attendee.
     *
     * @return The unique ID associated with this attendee.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the attendee.
     *
     * @param id The unique ID to be assigned to this attendee.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the document identifier of the attendee in the database.
     *
     * @return The document ID associated with this attendee.
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * Sets the document identifier of the attendee.
     *
     * @param documentId The document ID to be assigned to this attendee.
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * Retrieves the name of the attendee.
     *
     * @return The name of this attendee. Returns "GuestT04" if the name was not set or is empty.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the attendee. If the provided name is null or empty, the name is set to "GuestT04".
     *
     * @param name The name to be assigned to this attendee.
     */
    public void setName(String name) {
        if (name == null || name.isEmpty())
        {
            this.name = "GuestT04";
        }
        else {
            this.name = name;
        }
    }

    /**
     * Retrieves the email address of the attendee.
     *
     * @return The email address of this attendee. Returns null if the email was not set or is empty.
     */
    public String getEmail() {
        return email;
    }


    /**
     * Sets the email address of the attendee. If the provided email is null or empty, the email is set to null.
     *
     * @param email The email address to be assigned to this attendee.
     */
    public void setEmail(String email) {
        if (email == null || email.isEmpty())
        {
            this.email = null;
        }
        else {
            this.email = email;
        }
    }

    /**
     * Retrieves the path to the profile picture of the attendee.
     *
     * @return The file path or URL to the profile picture of this attendee.
     */
    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    /**
     * Sets the path to the profile picture of the attendee.
     *
     * @param profilePicturePath The file path or URL to the profile picture to be assigned to this attendee.
     */
    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    /**
     * Retrieves the geolocation status of the attendee.
     *
     * @return A Boolean indicating whether geolocation is enabled for this attendee.
     */
    public Boolean getGeolocationOn() {
        return geolocationOn;
    }

    /**
     * Sets the geolocation status for the attendee.
     *
     * @param geolocationOn The Boolean status to enable or disable geolocation for this attendee.
     */
    public void setGeolocationOn(Boolean geolocationOn) {
        this.geolocationOn = geolocationOn;
    }

    /**
     * Retrieves the number of check-ins made by the attendee.
     *
     * @return The total count of check-ins.
     */
    public long getCheckins() {
        return checkins;
    }

    /**
     * Sets the number of check-ins for the attendee.
     *
     * @param checkins The total count of check-ins to be assigned to this attendee.
     */
    public void setCheckins(long checkins) {
        this.checkins = checkins;
    }
}
