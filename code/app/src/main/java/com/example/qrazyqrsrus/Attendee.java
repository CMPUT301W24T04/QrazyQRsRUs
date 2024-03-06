package com.example.qrazyqrsrus;

import java.io.Serializable;

public class Attendee implements Serializable {
    private String name;
    private String num_checkins;
    private String id;
    private Boolean geolocation;

    public Attendee(String id, String name, String num_checkins, Boolean geolocation) {
        /**
         * Holds attendee information
         */
        this.id = id;
        this.name = name;
        this.num_checkins = num_checkins;
        this.geolocation = geolocation;
    }

    public String getName() {
        /**
         * gets attendee name
         */
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Boolean geolocation) {
        this.geolocation = geolocation;
    }

    public void setName(String name) {
        /**
         * sets attendee name
         */
        this.name = name;
    }

    public String getNum_checkins() {
        /**
         * gets number of checkins for attendee
         */
        return num_checkins;
    }

    public void setNum_checkins(String num_checkins) {
        /**
         * sets number of checkins for attendee
         */
        this.num_checkins = num_checkins;
    }

    // CREATE A LIST OF EVENTS THE USER TO IN AS AN ATTRIBUTE
}

