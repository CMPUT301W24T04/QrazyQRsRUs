package com.example.qrazyqrsrus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

class AttendeeSignUpsListAdapterTest {
    ArrayList<Attendee> MockAttendeeDataList = new ArrayList<>();
    Attendee mockAttendee = Mockito.mock(Attendee.class);

    private Attendee mockAttendee(){
        return new Attendee("1", "ewifnkw", "John", "john@ualberta.ca", "111111111111111", true,8);
    }
    @Test
    void getView() {

        mockAttendee.setName("Rao");
        mockAttendee.setCheckins(8);
        MockAttendeeDataList.add(mockAttendee);

        assertNotNull(MockAttendeeDataList);


    }

    @org.junit.Test
    public void TestListContents(){
        MockAttendeeDataList.add(mockAttendee());
        assertEquals(MockAttendeeDataList.get(0).getName(), "John");
    }
}