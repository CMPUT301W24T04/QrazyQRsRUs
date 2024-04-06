// This class tests if the attributes are being applied to and outputted from the CheckIn class
package com.example.qrazyqrsrus;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

public class CheckInTest {
    CheckIn checkin;
    /**
     * Creates the mock object to be added to the list
     * @return CheckIn
     */
    @Before
    public void setUp() {
        checkin = new CheckIn("attendeeID", "documentID", "eventID", 56.0000, 135.0000, 15);
    }

    /**
     * Test setters and getters for AttendeeDocId
     */
    @Test
    public void getsetAttendeeDocId() {
        checkin.setAttendeeDocId("attendeeID");
        assertEquals("attendeeID", checkin.getAttendeeDocId());
    }
    /**
     * Test setters and getters for Longitude
     */
    @Test
    public void getsetLongitude() {
        checkin.setLongitude(56.0000);
        assertEquals(56.0000, checkin.getLongitude(), 0);
    }
    /**
     * Test setters and getters for Latitude
     */
    @Test
    public void getsetLatitude() {
        checkin.setLatitude(56.0000);
        assertEquals(56.0000, checkin.getLatitude(), 0);
    }
    /**
     * Test setters and getters for Number of Checkins
     */
    @Test
    public void getsetNumberOfCheckIns() {
        checkin.setNumberOfCheckIns(15);
        assertEquals(15, checkin.getNumberOfCheckIns());
    }
    /**
     * Test setters and getters for DocumentId
     */
    @Test
    public void getsetDocumentId() {
        checkin.setAttendeeDocId("documentID");
        assertEquals("documentID", checkin.getDocumentId());
    }
    /**
     * Test setters and getters for EventId
     */
    @Test
    public void getsetEventDocId() {
        checkin.setAttendeeDocId("eventID");
        assertEquals("eventID", checkin.getEventDocId());
    }
    /**
     * Test if number of check ins increments correctly
     */
    @Test
    public void incrementCheckIn() {
        long increment_checkin;
        checkin.setNumberOfCheckIns(5);
        increment_checkin = checkin.getNumberOfCheckIns()+1;
        assertEquals(increment_checkin , checkin.getNumberOfCheckIns()+1);
    }


}