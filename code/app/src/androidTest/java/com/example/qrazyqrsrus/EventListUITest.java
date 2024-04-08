package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;


import org.junit.Test;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

public class EventListUITest {

    FirebaseDB mockFirebaseDB = Mockito.mock(FirebaseDB.class);
    ArrayList<String> announcements = new ArrayList<>();
    ArrayList<String> signUps = new ArrayList<>();
    ArrayList<String> checkIns = new ArrayList<>();

    private Event mockEvent(){
        return new Event("1111111", "John", "222222222", "This is an event",
                "Edmonton", "4/6/2024", "4/8/2024",
                true, "path", "qrCode",
                "promoQr", "organizerToken",announcements,signUps,
                checkIns, 10);
    }

    @Test
    public void testFragmentDisplayed() {

        Bundle bundle = getBundle();

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                ArrayList<Event> eventDataList = (ArrayList<Event>) args[0];
                EventListAdapter eventListAdapter = (EventListAdapter) args[1];

                eventDataList.add(mockEvent());
                eventListAdapter.notifyDataSetChanged();
                return null;
            }
        }).when(mockFirebaseDB).getAllEvents(Mockito.any(),Mockito.any());

        // Launch the fragment with the provided bundle
        FragmentScenario<EventList> scenario =  FragmentScenario.launchInContainer(EventList.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);

        scenario.onFragment(fragment ->
                fragment.setFirebaseDB(mockFirebaseDB)
        );
        scenario.onFragment(fragment ->
                scenario.moveToState(Lifecycle.State.RESUMED)
        );

//        Mockito.doNothing().when(mockFirebase).getEventCheckedInUsers(new Event(), MockAttendeeDataList, MockAttendeeListAdapter);
//        Mockito.doNothing().when(mockFirebaseDB).getEventCheckedInUsers(Mockito.any(),Mockito.any(),Mockito.any());
//        mockAttendee().setCheckins(8);
        onView(withId(R.id.event_list_title)).check(matches(isDisplayed()));
        onView(withId(R.id.event_list_view)).check(matches(isDisplayed()));
//        onView(withText("Name")).check(matches(isDisplayed()));
        onView(withId(R.id.event_list_view)).check(matches(hasChildCount(1)));
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
