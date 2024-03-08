package com.example.qrazyqrsrus;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttendeeTest {
    private Attendee attendee;

    @Before
    public void setUp() {
        attendee = new Attendee("1", "doc123", "John Doe", "john@example.com", "path/to/profile.jpg", true);
    }

    @Test
    public void testIdGetterSetter() {
        attendee.setId("2");
        assertEquals("2", attendee.getId());
    }

    @Test
    public void testDocumentIdGetterSetter() {
        attendee.setDocumentId("doc456");
        assertEquals("doc456", attendee.getDocumentId());
    }

    @Test
    public void testNameGetterSetter() {
        attendee.setName("Jane Doe");
        assertEquals("Jane Doe", attendee.getName());
    }

    @Test
    public void testEmailGetterSetter() {
        attendee.setEmail("jane@example.com");
        assertEquals("jane@example.com", attendee.getEmail());
    }

    @Test
    public void testProfilePicturePathGetterSetter() {
        attendee.setProfilePicturePath("path/to/newprofile.jpg");
        assertEquals("path/to/newprofile.jpg", attendee.getProfilePicturePath());
    }

    @Test
    public void testGeolocationOnGetterSetter() {
        attendee.setGeolocationOn(false);
        assertFalse(attendee.getGeolocationOn());
    }
}