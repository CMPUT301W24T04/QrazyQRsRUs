// This function contains test cases for the AttendeeListAdapter class
package com.example.qrazyqrsrus;

import static org.junit.jupiter.api.Assertions.*;

import android.widget.ArrayAdapter;

//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class AttendeeListAdapterTest{
    ArrayList<Attendee> MockAttendeeDataList = new ArrayList<>();
    Attendee mockAttendee1 = Mockito.mock(Attendee.class);

    private Attendee mockAttendee(){
        return new Attendee("1", "ewifnkw", "John", "john@ualberta.ca", "111111111111111", true,8);
    }
    @Test
    public void getView() {

        MockAttendeeDataList.add(mockAttendee());

        assertNotNull(MockAttendeeDataList);

        assertEquals(MockAttendeeDataList.get(0).getName(), "John");
        assertEquals(MockAttendeeDataList.get(0).getCheckins(), 8);

    }
}