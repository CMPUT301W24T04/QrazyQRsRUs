// This function contains test cases for the SignUpsList fragment
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

    /**
     * Creates the mock object to be added to the list
     * @return Attendee
     */
    private Attendee mockAttendee(){
        return new Attendee("1", "documentId", "John", "john@ualberta.ca", "111111111111111", true);
    }
    /**
     * Checks if the correct object can be added to the list
     */
    @Test
    public void onCreateView() {

        MockAttendeeDataList.add(mockAttendee1);

        assertNotNull(MockAttendeeDataList);
    }
    /**
     * Check if object has the correct attributes when added
     */
    @Test
    public void TestListContents(){
        MockAttendeeDataList.add(mockAttendee());
        assertEquals(MockAttendeeDataList.get(0).getName(), "John");
        assertEquals(MockAttendeeDataList.get(0).getId(), "1");
        assertEquals(MockAttendeeDataList.get(0).getDocumentId(), "documentId");
        assertEquals(MockAttendeeDataList.get(0).getEmail(), "john@ualberta.ca");
        assertEquals(MockAttendeeDataList.get(0).getProfilePicturePath(), "111111111111111");
        assertEquals(MockAttendeeDataList.get(0).getGeolocationOn(), true);

    }
    /**
     * Checks if the firebase function used in the class can be called
     */
    @Test
    public void TestFirebaseCalls(){
        //https://stackoverflow.com/questions/9841623/mockito-how-to-verify-method-was-called-on-an-object-created-within-a-method
        //accessed 4/6/2024
        mockFirebase.getEventSignedUpUsers(mockEvent, MockAttendeeDataList, MockAttendeeListAdapter);
        verify(mockFirebase).getEventSignedUpUsers(mockEvent, MockAttendeeDataList, MockAttendeeListAdapter);
    }
}