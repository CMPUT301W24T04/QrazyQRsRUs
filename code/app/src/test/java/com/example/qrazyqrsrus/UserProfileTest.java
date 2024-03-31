package com.example.qrazyqrsrus;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
//unit test checks if the UserProfile class correctly handles the details
public class UserProfileTest {
    private UserProfile userProfile;

    @Before
    public void setUp() {
        userProfile = new UserProfile();
        userProfile.setName("Test Name");
        userProfile.setAge("25");
        userProfile.setEmail("test@example.com");
        userProfile.setGeolocationOn(true);
    }

    @Test
    public void testNameGetterSetter() {
        assertEquals("Test Name", userProfile.getName());
    }

    @Test
    public void testAgeGetterSetter() {
        assertEquals("25", userProfile.getAge());
    }

    @Test
    public void testEmailGetterSetter() {
        assertEquals("test@example.com", userProfile.getEmail());
    }

    @Test
    public void testGeolocationOnGetterSetter() {
        assertTrue(userProfile.isGeolocationOn());
    }
}
