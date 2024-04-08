package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class EventDetailsFragmentTest{

    FirebaseDB mockFirebaseDB = Mockito.mock(FirebaseDB.class);
    NavController mockNavController;
    FragmentScenario<EventDetailsFragment> scenario;

    Event event;

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
        }).when(mockFirebaseDB).loginUser(any(), any());

        Mockito.doAnswer(invocation -> null).when(mockFirebaseDB).updateUser(any());
        Mockito.doAnswer(invocation -> null).when(mockFirebaseDB).subscribeAttendeeToEventTopic(any());
        Mockito.doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            this.event = getEvent(1);
            FirebaseDB.GetStringCallBack callBack = (FirebaseDB.GetStringCallBack) args[1];
            callBack.onResult("John Cena");

            return null;
        }).when(mockFirebaseDB).getUserName(any(), any());

        Mockito.doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            this.event = getEvent(1);
            FirebaseDB.GetBitmapCallBack callBack = (FirebaseDB.GetBitmapCallBack) args[1];
            callBack.onResult(null);

            return null;
        }).when(mockFirebaseDB).retrieveImage((Attendee) any(), any());

        // Launch the fragment with the provided bundle
        scenario =  FragmentScenario.launchInContainer(EventDetailsFragment.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);

        scenario.onFragment(eventDetailsFragment -> eventDetailsFragment.setFirebaseDB(mockFirebaseDB));
        scenario.onFragment(eventDetailsFragment -> scenario.moveToState(Lifecycle.State.RESUMED));
        scenario.onFragment((FragmentScenario.FragmentAction<EventDetailsFragment>) eventDetailsFragment -> Navigation.setViewNavController(eventDetailsFragment.requireView(), mockNavController));
    }
    public Attendee mockAttendee() {
        return new Attendee("1", "OrganizerID", "John Doe", "john@example.com", null, true);
    }

    public static Event getEvent(Integer count) {

        return new Event(null, "Event" + count, "OrganizerID", "Event Details", "Event Location",
                "testStartDate", "testEndDate", true, null, "DIFWDSADW", "DIFWDWDSSADW", null, null, new ArrayList<String>(), new ArrayList<String>(), 2);
    }

    @Test
    public void testFragmentDisplayed() {
        onView(withId(R.id.event_detail_name)).check(matches(isDisplayed()));
        onView(withId(R.id.event_detail_organizer)).check(matches(isDisplayed()));
        onView(withId(R.id.event_detail_details)).check(matches(isDisplayed()));
        onView(withId(R.id.event_detail_location)).check(matches(isDisplayed()));
        onView(withId(R.id.event_detail_start_date)).check(matches(isDisplayed()));
        onView(withId(R.id.event_detail_end_date)).check(matches(isDisplayed()));
        onView(withId(R.id.event_detail_end_date)).check(matches(isDisplayed()));

        onView(withId(R.id.sign_up_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.check_in_share_button)).check(matches(isDisplayed()));
        onView(withId(R.id.view_announcements_button)).check(matches(isDisplayed()));
        onView(withId(R.id.attendee_list_button)).check(matches(isDisplayed()));
        onView(withId(R.id.button_geolocation)).check(matches(isDisplayed()));

        onView(withId(R.id.event_detail_name)).check(matches(withText("Event1")));
        onView(withId(R.id.event_detail_location)).check(matches(withText("Location:     Event Location")));
        onView(withId(R.id.event_detail_organizer)).check(matches(withText("Organizer:     John Cena")));

    }

    @Test
    public void testButtonsClicked() {
        onView(withId(R.id.view_announcements_button)).perform(click());
        onView(withId(R.id.attendee_list_button)).perform(click());
        onView(withId(R.id.button_geolocation)).perform(click());

        verify(mockNavController).navigate(eq(R.id.action_eventDetailsFragment_to_attendeeList2), any(Bundle.class));
        verify(mockNavController).navigate(eq(R.id.action_eventDetailsFragment_to_geoLocation), any(Bundle.class));
        verify(mockNavController).navigate(eq(R.id.action_eventDetailsFragment_to_AnnouncementEditFragment), any(Bundle.class));
    }

    @NonNull
    private static Bundle getBundle() {
        Event event = getEvent(1);
        // Create a bundle with necessary data
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);
        bundle.putBoolean("isCheckedIn", false);
        return bundle;
    }
}
