// This function contains test cases for the EventList fragment
package com.example.qrazyqrsrus;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.verify;

import org.mockito.Mockito;

import org.junit.Test;

import java.util.ArrayList;

public class EventListTest extends AttendeeList{
    ArrayList<Event> MockEventDataList = new ArrayList<>();
    ArrayList<String> announcements = new ArrayList<>();
    ArrayList<String> signUps = new ArrayList<>();
    ArrayList<String> checkIns = new ArrayList<>();

    EventListAdapter MockEventListAdapter; // = new AttendeeListAdapter(ArrayList, MockAttendeeDataList);

    Event mockEvent = Mockito.mock(Event.class);
    FirebaseDB mockFirebase = Mockito.mock(FirebaseDB.class);

    /**
     * Creates the mock object to be added to the list
     * @return Event
     */
    private Event mockEvent(){
        return new Event("1111111", "John", "222222222", "This is an event",
                "Edmonton", "4/6/2024", "4/8/2024",
                true, "path", "qrCode",
                "promoQr", "organizerToken",announcements,signUps,
                checkIns, 10);
    }
    /**
     * Checks if the correct object can be added to the list
     */
    @Test
    public void onCreateView() {

        MockEventDataList.add(mockEvent());

        assertNotNull(MockEventDataList);
    }
    /**
     * Check if object has the correct attributes when added
     */
    @Test
    public void TestListContents(){
        MockEventDataList.add(mockEvent());
        assertEquals(MockEventDataList.get(0).getName(), "John");
        assertEquals(MockEventDataList.get(0).getSignUps(), signUps);
    }
    /**
     * Checks if the firebase function used in the class can be called
     */
    @Test
    public void TestFirebaseCalls(){
        //https://stackoverflow.com/questions/9841623/mockito-how-to-verify-method-was-called-on-an-object-created-within-a-method
        //accessed 4/6/2024
        mockFirebase.getAllEvents(MockEventDataList, MockEventListAdapter);
        verify(mockFirebase).getAllEvents(MockEventDataList, MockEventListAdapter);
    }
}