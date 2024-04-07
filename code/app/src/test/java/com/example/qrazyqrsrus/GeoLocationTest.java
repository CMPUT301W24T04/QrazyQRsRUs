// This function contains test cases for the SignUpsList fragment
package com.example.qrazyqrsrus;

import static org.junit.jupiter.api.Assertions.*;

import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.Firebase;

import org.junit.Before;
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

import javax.security.auth.callback.Callback;

public class GeoLocationTest {
//    public interface GetMapMarkersCallback {
//        void onResult(ArrayList<CheckIn> checks, ArrayList<String> names);
//    }
    ArrayList<Attendee> MockAttendeeDataList = new ArrayList<>();
    ArrayList<CheckIn> MockCheckinDataList = new ArrayList<>();
    Event mockEvent = Mockito.mock(Event.class);

    interface GetMapMarkersCallback{};

//    GetMapMarkersCallback mockCallback = Mockito.mock(GetMapMarkersCallback.class);
    FirebaseDB mockFirebase = Mockito.mock(FirebaseDB.class);

    /**
     * Creates the mock object to be added to the list
     * @return Attendee
     */
    private Attendee mockAttendee(){
        return new Attendee("1", "ewifnkw", "John", "john@ualberta.ca", "111111111111111", true);
    }
    /**
     * Creates the mock object to be added to the list
     * @return CheckIn
     */
    public CheckIn mockCheckin() {
        return new CheckIn("attendeeID", "documentID", "eventID", 56.0000, 135.0000, 15);
    }

    @Test
    public void onCreateView() {
        MockAttendeeDataList.add(mockAttendee());
        assertNotNull(MockAttendeeDataList);

        MockCheckinDataList.add(mockCheckin());
        assertNotNull(MockCheckinDataList);
    }

    /**
     * Checks if the firebase function used in the class can be called
     */
    @Test
    public void TestFirebaseCalls(){
        //https://stackoverflow.com/questions/9841623/mockito-how-to-verify-method-was-called-on-an-object-created-within-a-method
        //accessed 4/6/2024
        mockFirebase.getGeolocations(mockEvent, Mockito.any(FirebaseDB.GetMapMarkersCallback.class));
        verify(mockFirebase).getGeolocations(mockEvent, Mockito.any(FirebaseDB.GetMapMarkersCallback.class));
    }
}