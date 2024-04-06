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
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;
import org.mockito.Mockito;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
//import org.junit.jupiter.api.Test;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import org.junit.Test;  // ADD THIS TO STOP ERROR

import java.util.ArrayList;

public class AttendeeListTest extends AttendeeList{
//    Attendee attendee1;
    ListView attendeeList;
    ArrayList<Attendee> MockAttendeeDataList = new ArrayList<>();

    AttendeeListAdapter MockAttendeeListAdapter; // = new AttendeeListAdapter(ArrayList, MockAttendeeDataList);

    Event mockEvent = Mockito.mock(Event.class);

    Attendee mockAttendee1 = Mockito.mock(Attendee.class);

    FirebaseDB mockFirebase = Mockito.mock(FirebaseDB.class);



    ArrayList<Attendee> attendeeFirebaseList;
    AttendeeListAdapter attendeeListAdapter;

    //    private AttendeeList mockAttendeeList(){
//        AttendeeList attendeeList = new AttendeeList();
//        attendeeList.add();
//        return attendeeList;
//    }

    private Attendee mockAttendee(){
        return new Attendee("1", "ewifnkw", "John", "john@ualberta.ca", "111111111111111", true,8);
    }
    @Test
    public void onCreateView() {
//        FirebaseDB mockFirebaseDB = Mockito.mock(FirebaseDB.class);
//        attendee1.setCheckins(8);
//        attendee1.setName("John");
//        attendee1.setDocumentId("ewifnkw");
//        attendee1.setEmail("john@ualberta.ca");
//        attendee1.setProfilePicturePath("111111111111111");
//        attendee1.setGeolocationOn(true);
//        attendee1.setId("1");

        //        Attendee attendee = new Attendee(id, documentId, name, email, profilePicturePath, geolocationOn, checkins);

//        Attendee attendee1 = new Attendee("1", "ewifnkw", "John", "john@ualberta.ca", "111111111111111", true,8);
//        attendeeDataList.add(attendee1);
//
//        assertEquals(2, attendeeDataList.size());
//        assertNotNull(attendeeDataList);

        MockAttendeeDataList.add(mockAttendee1);

        assertNotNull(MockAttendeeDataList);



//        when(mockFirebaseDB.class).then;
//
//        mockFirebaseDB.getEventCheckedInUsers(Event event, ArrayList<Attendee> attendeeDataList, ArrayAdapter<Attendee> attendeeListAdapter);
    }

    @Test
    public void TestListContents(){
        MockAttendeeDataList.add(mockAttendee());
        assertEquals(MockAttendeeDataList.get(0).getName(), "John");
        assertEquals(MockAttendeeDataList.get(0).getCheckins(), 8);
    }

    @Test
    public void TestFirebaseCalls(){
        //https://stackoverflow.com/questions/9841623/mockito-how-to-verify-method-was-called-on-an-object-created-within-a-method
        //accessed 4/6/2024
        mockFirebase.getEventCheckedInUsers(mockEvent, MockAttendeeDataList, MockAttendeeListAdapter);
        verify(mockFirebase).getEventCheckedInUsers(mockEvent, MockAttendeeDataList, MockAttendeeListAdapter);
    }
}