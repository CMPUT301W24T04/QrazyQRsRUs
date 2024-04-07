package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class AttendeeListUITest {
    FirebaseDB mockFirebase = Mockito.mock(FirebaseDB.class);
    ArrayList<Attendee> MockAttendeeDataList = new ArrayList<>();
    AttendeeListAdapter MockAttendeeListAdapter;
    private Attendee mockAttendee(){
        return new Attendee("1", "DocumentId", "John", "john@ualberta.ca", "111111111111111", true,8);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testFragmentDisplayed() {
//        Mockito.doNothing().when(mockFirebase).getEventCheckedInUsers(new Event(), MockAttendeeDataList, MockAttendeeListAdapter);
        Mockito.doNothing().when(mockFirebase).getEventCheckedInUsers(Mockito.any(),Mockito.any(),Mockito.any());
        Bundle bundle = getBundle();

        // Launch the fragment with the provided bundle
        FragmentScenario<AttendeeList> scenario =  FragmentScenario.launchInContainer(AttendeeList.class, bundle, R.style.Base_Theme_QrazyQRsRUs);

        Mockito.doNothing().when(mockFirebase).getEventCheckedInUsers(new Event(), MockAttendeeDataList, MockAttendeeListAdapter);


        scenario.onFragment(new FragmentScenario.FragmentAction<AttendeeList>() {
            @Override
            public void perform(@NonNull AttendeeList attendeeList) {
                mockAttendee().setCheckins(8);
//                Mockito.doNothing().when(mockFirebase).getEventCheckedInUsers(new Event(), MockAttendeeDataList, MockAttendeeListAdapter);
                Mockito.doNothing().when(mockFirebase).getEventCheckedInUsers(Mockito.any(),Mockito.any(),Mockito.any());

                onView(withId(R.id.button_view_signups)).check(matches(isDisplayed()));
                onView(withId(R.id.button_back_checkin)).check(matches(isDisplayed()));
                onView(withId(R.id.app_title)).check(matches(isDisplayed()));
                onView(withId(R.id.attendee_list_view)).check(matches(isDisplayed()));
                onView(withId(R.id.attendee_list_view)).check(matches(hasChildCount(0)));
            }
        });

    }

    @NonNull
    private static Bundle getBundle() {
        ArrayList<String> announcements = new ArrayList<>();
        ArrayList<String> signUps = new ArrayList<>();
        ArrayList<String> checkIns = new ArrayList<>();
        checkIns.add("uHWRJuNzQmzjXa1dfleb");
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
