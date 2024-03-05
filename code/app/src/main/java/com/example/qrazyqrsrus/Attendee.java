package com.example.qrazyqrsrus;

import java.io.Serializable;

public class Attendee implements Serializable {
    private String name;
    private String num_checkins;

    public Attendee(String name, String num_checkins) {
        /**
         * Holds attendee information
         */
        this.name = name;
        this.num_checkins = num_checkins;
    }

    public String getName() {
        /**
         * gets attendee name
         */
        return name;
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

