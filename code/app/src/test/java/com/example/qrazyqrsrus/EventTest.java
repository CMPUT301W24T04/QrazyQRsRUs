package com.example.qrazyqrsrus;

import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.Assert.*;
// test method that verifies the getters and setters for each field in the Event class.
//  This verifies all the assertion checks that the value set by the setter is correctly returned by the getter.
public class EventTest {
    private Event event;
    private LocalDateTime testStartDate;
    private LocalDateTime testEndDate;

    @Before
    public void setUp() {
        testStartDate = LocalDateTime.of(2024, 3, 7, 12, 0);
        testEndDate = LocalDateTime.of(2024, 3, 8, 12, 0);
        event = new Event("Test Event", "OrganizerID", "Event Details", "Event Location",
                testStartDate, testEndDate);
    }
    //the testGettersAndSetters are systematically sets each attribute of the Event object using the
// setters and then asserts that the getters return the correct values.
//the test also check the behaviors of the list by adding items and deleting them
// check that these operations result in the expected changes to the lists.
    @Test
    public void testGettersAndSetters() {
        // Test each property with its getter and setter
        event.setDocumentId("Doc123");
        assertEquals("Doc123", event.getDocumentId());

        event.setName("Updated Event Name");
        assertEquals("Updated Event Name", event.getName());

        event.setOrganizerId("NewOrganizerID");
        assertEquals("NewOrganizerID", event.getOrganizerId());

        event.setDetails("Updated Event Details");
        assertEquals("Updated Event Details", event.getDetails());

        event.setLocation("New Location");
        assertEquals("New Location", event.getLocation());

        // Start and end dates are set in setUp and formatted to String
        assertEquals("2024-03-07 12:00", event.getStartDate());
        assertEquals("2024-03-08 12:00", event.getEndDate());

        event.setGeolocationOn(false);
        assertFalse(event.getGeolocationOn());

        event.setPosterPath("New Poster Path");
        assertEquals("New Poster Path", event.getPosterPath());

        event.setQrCode("NewQRCode");
        assertEquals("NewQRCode", event.getQrCode());

        event.setQrCodePromo("NewQRPromoCode");
        assertEquals("NewQRPromoCode", event.getQrCodePromo());

        ArrayList<String> testAnnouncements = new ArrayList<>();
        testAnnouncements.add("Announcement 1");
        event.setAnnouncements(testAnnouncements);
        assertEquals(testAnnouncements, event.getAnnouncements());
        event.addAnnouncement("Announcement 2");
        assertTrue(event.getAnnouncements().contains("Announcement 2"));

        ArrayList<String> testSignUps = new ArrayList<>();
        testSignUps.add("SignUp 1");
        event.setSignUps(testSignUps);
        assertEquals(testSignUps, event.getSignUps());
        event.addSignUp("SignUp 2");
        assertTrue(event.getSignUps().contains("SignUp 2"));
        event.deleteSignUp("SignUp 2");
        assertFalse(event.getSignUps().contains("SignUp 2"));

        ArrayList<String> testCheckIns = new ArrayList<>();
        testCheckIns.add("CheckIn 1");
        event.setCheckIns(testCheckIns);
        assertEquals(testCheckIns, event.getCheckIns());
        event.addCheckIn("CheckIn 2");
        assertTrue(event.getCheckIns().contains("CheckIn 2"));
        event.deleteCheckIn("CheckIn 2");
        assertFalse(event.getCheckIns().contains("CheckIn 2"));
    }
}
