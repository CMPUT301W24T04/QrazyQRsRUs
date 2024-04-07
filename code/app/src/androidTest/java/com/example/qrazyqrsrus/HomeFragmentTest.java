package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;


public class HomeFragmentTest {

    FirebaseDB mockFirebaseDB = Mockito.mock(FirebaseDB.class);
    NavController mockNavController;
    FragmentScenario<HomeFragment> scenario;

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockNavController = Mockito.mock(NavController.class);

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

        Mockito.doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ArrayList<Event> MockCheckInsList = (ArrayList<Event>) args[1];
            HomeSignedUpListAdapter homeSignedUpListAdapter = (HomeSignedUpListAdapter) args[2];

            MockCheckInsList.add(getEvent(3));
            MockCheckInsList.add(getEvent(4));
            homeSignedUpListAdapter.notifyDataSetChanged();
            return null;
        }).when(mockFirebaseDB).getAttendeeSignedUpEvents(Mockito.any(), Mockito.any(), Mockito.any());

        // Launch the fragment with the provided bundle
        scenario =  FragmentScenario.launchInContainer(HomeFragment.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);

        scenario.onFragment(homeFragment -> homeFragment.setFirebaseDB(mockFirebaseDB));
        scenario.onFragment(homeFragment -> scenario.moveToState(Lifecycle.State.RESUMED));
        scenario.onFragment((FragmentScenario.FragmentAction<HomeFragment>) homeFragment -> Navigation.setViewNavController(homeFragment.requireView(), mockNavController));
    }
    public Attendee mockAttendee() {
        return new Attendee("1", "doc123", "John Doe", "john@example.com", null, true);
    }

    public Event getEvent(Integer count) {

        return new Event(null, "Event" + count, "OrganizerID", "Event Details", "Event Location",
                "testStartDate", "testEndDate", true, null, "DIFWDSADW", "DIFWDWDSSADW", null, null, null, null, 2);
    }
    @Test
    public void testFragmentDisplayed() {
        onView(withId(R.id.browse_events_button)).check(matches(isDisplayed()));
        onView(withId(R.id.checked_in_events_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.signed_up_events_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.events_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.welcome_message_view)).check(matches(isDisplayed()));
        onView(withId(R.id.checked_in_events_listview)).check(matches(hasChildCount(2)));
        onView(withId(R.id.signed_up_events_listview)).check(matches(hasChildCount(2)));
    }

    @Test
    public void testBrowseButton() {
        onView(withId(R.id.browse_events_button)).perform(click());

        verify(mockNavController).navigate(eq(R.id.action_homeFragment_to_eventList3));
    }

    @NonNull
    private Bundle getBundle() {
        Attendee attendee = mockAttendee();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", attendee);
        return bundle;
    }

}