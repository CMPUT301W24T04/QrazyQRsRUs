// This function contains test cases for the EventListAdapter class
package com.example.qrazyqrsrus;

import static org.junit.jupiter.api.Assertions.*;

import android.widget.ArrayAdapter;

//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class EventListAdapterTest{
    ArrayList<Event> MockEventDataList = new ArrayList<>();
    ArrayList<String> announcements = new ArrayList<>();
    ArrayList<String> signUps = new ArrayList<>();
    ArrayList<String> checkIns = new ArrayList<>();
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
    public void getView() {

        MockEventDataList.add(mockEvent());

        assertNotNull(MockEventDataList);
    }
    /**
     * Test if the right attributes are being taken from the event list
     */
    @Test
    public void TestObjectAttributes(){
        MockEventDataList.add(mockEvent());
        assertEquals(MockEventDataList.get(0).getName(), "John");
        assertEquals(MockEventDataList.get(0).getDocumentId(), "1111111");
        assertEquals(MockEventDataList.get(0).getOrganizerId(), "222222222");
        assertEquals(MockEventDataList.get(0).getDetails(), "This is an event");
        assertEquals(MockEventDataList.get(0).getLocation(), "Edmonton");
        assertEquals(MockEventDataList.get(0).getStartDate(), "4/6/2024");
        assertEquals(MockEventDataList.get(0).getEndDate(), "4/8/2024");
        assertEquals(MockEventDataList.get(0).getGeolocationOn(), true);
        assertEquals(MockEventDataList.get(0).getPosterPath(), "path");
        assertEquals(MockEventDataList.get(0).getQrCode(), "qrCode");
        assertEquals(MockEventDataList.get(0).getQrCodePromo(), "promoQr");
        assertEquals(MockEventDataList.get(0).getOrganizerToken(), "organizerToken");
        assertEquals(MockEventDataList.get(0).getAnnouncements(), announcements);
        assertEquals(MockEventDataList.get(0).getSignUps(), signUps);
        assertEquals(MockEventDataList.get(0).getCheckIns(), checkIns);
//        assertEquals(MockEventDataList.get(0).getMaxAttendees(), 10);

    }
}