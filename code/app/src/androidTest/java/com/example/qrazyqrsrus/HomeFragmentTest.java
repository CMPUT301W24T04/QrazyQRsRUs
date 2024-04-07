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
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;


public class HomeFragmentTest {

    FirebaseDB mockFirebaseDB = Mockito.mock(FirebaseDB.class);

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    public Attendee mockAttendee() {
        return new Attendee("1", "doc123", "John Doe", "john@example.com", null, true);
    }

    public Event getEvent(Integer count) {

        return new Event(null, "Event" + count, "OrganizerID", "Event Details", "Event Location",
                "testStartDate", "testEndDate", true, null, "DIFWDSADW", "DIFWDWDSSADW", null, null, null, null, 2);
    }
    @Test
    public void testFragmentDisplayed() {

        Bundle bundle = getBundle();

        Mockito.doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            FirebaseDB.GetAttendeeCallBack callBack = (FirebaseDB.GetAttendeeCallBack) args[1];

            callBack.onResult(mockAttendee());
            return null;
        }).when(mockFirebaseDB).loginUser(Mockito.any(), Mockito.any());

        Mockito.doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ArrayList<Event> MockCheckInsList = (ArrayList<Event>) args[1];
            HomeCheckedInListAdapter MockCheckInsListAdapter = (HomeCheckedInListAdapter) args[2];

            MockCheckInsList.add(getEvent(1));
            MockCheckInsList.add(getEvent(2));
            MockCheckInsListAdapter.notifyDataSetChanged();
            return null;
        }).when(mockFirebaseDB).getEventsCheckedIn(Mockito.any(), Mockito.any(), Mockito.any());

        // Launch the fragment with the provided bundle
        FragmentScenario<HomeFragment> scenario =  FragmentScenario.launchInContainer(HomeFragment.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);

        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull HomeFragment homeFragment) {
                homeFragment.setFirebaseDB(mockFirebaseDB);
                scenario.moveToState(Lifecycle.State.RESUMED);
            }
        });

        onView(withId(R.id.browse_events_button)).check(matches(isDisplayed()));
        onView(withId(R.id.checked_in_events_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.signed_up_events_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.events_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.welcome_message_view)).check(matches(isDisplayed()));
        onView(withId(R.id.checked_in_events_listview)).check(matches(hasChildCount(0)));
        onView(withId(R.id.signed_up_events_listview)).check(matches(hasChildCount(0)));



    }

    @NonNull
    private Bundle getBundle() {
        Attendee attendee = mockAttendee();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", attendee);
        return bundle;
    }

}