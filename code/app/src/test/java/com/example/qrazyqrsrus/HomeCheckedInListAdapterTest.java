package com.example.qrazyqrsrus;
import static org.junit.jupiter.api.Assertions.*;

import androidx.test.core.app.ApplicationProvider;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class HomeCheckedInListAdapterTest {

    private Event mockEvent(){
        return new Event(null, "Test Event", "OrganizerID", "Event Details", "Event Location",
                "testStartDate", "testEndDate", true, null, null, null, null, null, null, null, 2);
    }

    @Test
    public void TestGetEventName(){
        Event event1 = mockEvent();
        Event event2 = mockEvent();
        Event event3 = mockEvent();

        event1.setName("Event1");
        event2.setName("Event2");
        event3.setName("Event3");

        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        events.add(event3);

        HomeCheckedInListAdapter adapter = new HomeCheckedInListAdapter(ApplicationProvider.getApplicationContext(), events);

        assertEquals("Event2", adapter.getEventName(2));
        assertEquals("Event3", adapter.getEventName(3));
        assertEquals("Event1", adapter.getEventName(1));
    }
}
