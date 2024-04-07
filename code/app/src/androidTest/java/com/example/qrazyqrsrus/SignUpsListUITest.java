package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

public class SignUpsListUITest {

    FirebaseDB mockFirebaseDB = Mockito.mock(FirebaseDB.class);
    ArrayList<Attendee> attendeeDataList = new ArrayList<>();
    AttendeeSignUpsListAdapter attendeeListAdapter;

    private Attendee mockAttendee(){
        return new Attendee("1", "DocumentId", "John", "john@ualberta.ca", "111111111111111", true);
    }

//    @Rule
//    public ActivityScenarioRule<MainActivity> rule =
//            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testFragmentDisplayed() {


        Bundle bundle = getBundle();
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                ArrayList<Attendee> attendeeDataList = (ArrayList<Attendee>) args[1];
                AttendeeSignUpsListAdapter attendeeListAdapter = (AttendeeSignUpsListAdapter) args[2];

                attendeeDataList.add(mockAttendee());
//                MockAttendeeListAdapter.setAdapterFirebaseDB(mockFirebaseDB);
                attendeeListAdapter.notifyDataSetChanged();
                return null;
            }
        }).when(mockFirebaseDB).getEventSignedUpUsers(Mockito.any(),Mockito.any(),Mockito.any());

        // Launch the fragment with the provided bundle
        FragmentScenario<AttendeeSignupsList> scenario =  FragmentScenario.launchInContainer(AttendeeSignupsList.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);

        scenario.onFragment(fragment ->
                fragment.setFirebaseDB(mockFirebaseDB)
        );
        scenario.onFragment(fragment ->
                scenario.moveToState(Lifecycle.State.RESUMED)
        );


        mockAttendee().setCheckins(8);
        onView(withId(R.id.button_back_signups)).check(matches(isDisplayed()));
        onView(withId(R.id.app_title)).check(matches(isDisplayed()));
        onView(withId(R.id.attendee_signups_list_view)).check(matches(isDisplayed()));

        onView(withId(R.id.attendee_signups_list_view)).check(matches(isDisplayed()));

        onView(withId(R.id.attendee_signups_list_view)).check(matches(hasChildCount(1)));

        onView(withText("John")).check(matches(isDisplayed()));

    }

    @Test
    public void TestListContents(){

    }

    @NonNull
    private static Bundle getBundle() {
        ArrayList<String> announcements = new ArrayList<>();
        ArrayList<String> signUps = new ArrayList<>();
        ArrayList<String> checkIns = new ArrayList<>();
        checkIns.add("uHWRJuNzQmzjXa1dfleb");
        signUps.add("uHWRJuNzQmzjXa1dfleb");
        Event event = new Event("1111111", "John", "222222222", "This is an event",
                "Edmonton", "4/6/2024", "4/8/2024",
                true, "path", "qrCode",
                "promoQr", "organizerToken",announcements,signUps,
                checkIns, 10);
        // Create a bundle with necessary data
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);
        return bundle;
    }

}
