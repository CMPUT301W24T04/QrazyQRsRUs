package com.example.qrazyqrsrus;

import static org.junit.jupiter.api.Assertions.*;

import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.Firebase;

import org.junit.Rule;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
//import org.junit.jupiter.api.Test;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import org.junit.Test;  // ADD THIS TO STOP ERROR

import java.util.ArrayList;

public class AttendeeSignupsListTest extends AttendeeList{
    ArrayList<Attendee> MockAttendeeDataList = new ArrayList<>();

    AttendeeSignUpsListAdapter MockAttendeeListAdapter; // = new AttendeeListAdapter(ArrayList, MockAttendeeDataList);

    Event mockEvent = Mockito.mock(Event.class);

    Attendee mockAttendee1 = Mockito.mock(Attendee.class);

    FirebaseDB mockFirebase = Mockito.mock(FirebaseDB.class);

    @Test
    public void onCreateView() {

        MockAttendeeDataList.add(mockAttendee1);

        assertNotNull(MockAttendeeDataList);
    }

    @Test
    public void TestFirebaseCalls(){
        //https://stackoverflow.com/questions/9841623/mockito-how-to-verify-method-was-called-on-an-object-created-within-a-method
        //accessed 4/6/2024
        mockFirebase.getEventSignedUpUsers(mockEvent, MockAttendeeDataList, MockAttendeeListAdapter);
        verify(mockFirebase).getEventSignedUpUsers(mockEvent, MockAttendeeDataList, MockAttendeeListAdapter);
    }
}