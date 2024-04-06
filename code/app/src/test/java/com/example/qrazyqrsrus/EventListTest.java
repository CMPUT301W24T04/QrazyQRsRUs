// This function contains test cases for the EventList fragment
package com.example.qrazyqrsrus;

import static org.junit.jupiter.api.Assertions.*;

import android.content.Context;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.Firebase;

import org.junit.Rule;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
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


    private Event mockEvent(){
        return new Event("1111111", "John", "222222222", "This is an event",
                "Edmonton", "4/6/2024", "4/8/2024",
                true, "path", "qrCode",
                "promoQr", "organizerToken",announcements,signUps,
                checkIns, 10);
    }
    @Test
    public void onCreateView() {

        MockEventDataList.add(mockEvent());

        assertNotNull(MockEventDataList);
    }

    @Test
    public void TestListContents(){
        MockEventDataList.add(mockEvent());
        assertEquals(MockEventDataList.get(0).getName(), "John");
        assertEquals(MockEventDataList.get(0).getSignUps(), signUps);
    }

    @Test
    public void TestFirebaseCalls(){
        //https://stackoverflow.com/questions/9841623/mockito-how-to-verify-method-was-called-on-an-object-created-within-a-method
        //accessed 4/6/2024
        mockFirebase.getAllEvents(MockEventDataList, MockEventListAdapter);
        verify(mockFirebase).getAllEvents(MockEventDataList, MockEventListAdapter);
    }
}