// This function contains test cases for the AttendeeSignUpsAdapter class
package com.example.qrazyqrsrus;

import static org.junit.jupiter.api.Assertions.*;

//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class HomeSignedUpListAdapterTest {
    ArrayList<Attendee> MockAttendeeDataList = new ArrayList<>();
    Attendee mockAttendee = Mockito.mock(Attendee.class);

    private Attendee mockAttendee(){
        return new Attendee("1", "ewifnkw", "John", "john@ualberta.ca", "111111111111111", true);
    }
    /**
     * Checks if the list can have the right object added to it
     */
    @Test
    public void getView() {

        MockAttendeeDataList.add(mockAttendee());

        assertNotNull(MockAttendeeDataList);
    }

    /**
     * Test if the correct attributes are being added to the list
     */
    @org.junit.Test
    public void TestListContents(){
        MockAttendeeDataList.add(mockAttendee());
        assertEquals(MockAttendeeDataList.get(0).getName(), "John");
        assertEquals(MockAttendeeDataList.get(0).getDocumentId(), "ewifnkw");
        assertEquals(MockAttendeeDataList.get(0).getId(), "1");
        assertEquals(MockAttendeeDataList.get(0).getProfilePicturePath(), "111111111111111");
        assertEquals(MockAttendeeDataList.get(0).getGeolocationOn(), true);
        assertEquals(MockAttendeeDataList.get(0).getName(), "John");
    }
}